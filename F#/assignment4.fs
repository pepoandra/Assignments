(* Assignment 4 *)
(* Sebastian Andrade. ID: 260513637*)


type id = string

type term =
  | Var of id
  | Const of int 
  | Term of id * term list

(* invariant for substitutions: *)
(* no id on a lhs occurs in any term earlier in the list *)
type substitution = (id * term) list

(* check if a variable occurs in a term *)
let rec occurs (x : id) (t : term) : bool = 
    match t with
    | Var y -> (x=y)
    | Const a -> false
    | Term (str, termList) ->  List.exists (occurs x) termList

   

(* substitute term s for all occurrences of variable x in term t *)
let rec subst (s : term) (x : id) (t : term) : term = 
                      match t with
                      | Const a -> t
                      | Var b -> if ( x=b ) then s else t
                      | Term( c, lst) -> Term (c , List.map (subst s x) lst ) 


(* apply a substitution right to left; use foldBack *)
let apply (s : substitution) (t : term) : term = 
 List.foldBack (fun (x, acc) -> (subst acc x)) s t


(* unify one pair *)
let rec unify (s : term) (t : term) : substitution =
  let rec helper lst1 lst2 =
    match (lst1, lst2) with
    | ([], (_)) | ((_), []) -> [] 
    | ([a],[b]) -> [(a,b)]
    | (x::xs , y::ys) -> [(x,y)]@ (helper xs ys)

  match (s, t) with
  | (Const a, Const b) -> if (a = b ) then [] else failwith "not unifiable: clashing constants"
  | ( Const d, Term(p, lst)) | (Term (p, lst), Const d) -> failwith "not unifiable: term constant clash"
  | ( Const c, Var y) -> [(y, s)]
  |  (Var y, Const c) -> [(y, t)] 
  | (Var x, Var y) -> if x = y then [] else [(x, t)]
  | (Term (f, lst1), Term (g, lst2)) ->
      if f = g && List.length lst1 = List.length lst2
      then unify_list (helper lst1 lst2)
      else failwith "not unifiable: head symbol conflict"
  | ((Var x, (Term (_) as t)) | ((Term (_) as t), Var x)) ->
      if occurs x t
      then failwith "not unifiable: circularity"
      else [(x, t)]

(* unify a list of pairs *)
and unify_list (s : (term * term) list) : substitution =
  match s with
  | [] -> []
  | (x, y) :: t ->
      let t2 = unify_list t in
      let t1 = unify (apply t2 x) (apply t2 y) in
      t1 @ t2



(*
Examples
> let t1 = Term("f",[Var "x";Var "y"; Term("h",[Var "x"])]);;
val t1 : term = Term ("f",[Var "x"; Var "y"; Term ("h",[Var "x"])])
> let t2 = Term("f", [Term("g",[Var "z"]); Term("h",[Var "x"]); Var "y"]);;
val t2 : term =
  Term ("f",[Term ("g",[Var "z"]); Term ("h",[Var "x"]); Var "y"])
> let t3 = Term("f", [Var "x"; Var "y"; Term("g", [Var "u"])]);;
val t3 : term = Term ("f",[Var "x"; Var "y"; Term ("g",[Var "u"])])
> unify t1 t2;;
val it : substitution =
  [("x", Term ("g",[Var "z"])); ("y", Term ("h",[Var "x"]))]
> let t4 = Term("f", [Var "x"; Term("h", [Var "z"]); Var "x"]);;
val t4 : term = Term ("f",[Var "x"; Term ("h",[Var "z"]); Var "x"])
>  let t5 = Term("f", [Term("k", [Var "y"]); Var "y"; Var "x"]);;
val t5 : term = Term ("f",[Term ("k",[Var "y"]); Var "y"; Var "x"])
> unify t4 t5;;
val it : substitution =
  [("x", Term ("k",[Term ("h",[Var "z"])])); ("y", Term ("h",[Var "z"]))]
> unify t5 t4;;
val it : substitution =
  [("x", Term ("k",[Term ("h",[Var "z"])])); ("y", Term ("h",[Var "z"]))]
> apply it t4;;
val it : term =
  Term
    ("f",
     [Term ("k",[Term ("h",[Var "z"])]); Term ("h",[Var "z"]);
      Term ("k",[Term ("h",[Var "z"])])])
> let t6 = Term("f", [Const 2; Var "x"; Const 3]);;
val t6 : term = Term ("f",[Const 2; Var "x"; Const 3])
> let t7 = Term("f", [Const 2; Const 3; Var "y"]);;
val t7 : term = Term ("f",[Const 2; Const 3; Var "y"])
> unify t6 t7;;
val it : substitution = [("x", Const 3); ("y", Const 3)]
> apply it t7;;
val it : term = Term ("f",[Const 2; Const 3; Const 3])
> unify t1 t7;;
System.Exception: not unifiable: term constant clash
....... junk removed .............
Stopped due to error
*)
