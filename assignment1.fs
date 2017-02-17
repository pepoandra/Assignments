
(* module hw1_sol.  Use this if you want to load the file into an interactive session.*)

(* Question 1 *) (* Do not edit this line. *)

let rec sumlist l = 
    match l with 
    | [] -> 0.0
    | x::xs -> x + (sumlist xs)

let rec pairlists (l1, l2) =
  match l1, l2 with
    | ([],[]) -> []
    | ([],x::xs) -> failwith "Error -- lists are not of the same length"
    | (x::xs, []) -> failwith "Error -- lists are not of the same length"
    | (x::xs, y::ys) -> [(x, y)]@pairlists(xs, ys)

let w_mean weights data =  
    let product = List.map (fun(x,y)-> x*y) (pairlists (weights, data))
    (sumlist product)/(sumlist weights)

(* Question 2. *)  (* Do not edit this line. *)

let rec memberof e l = 
    match l with
    | [] -> false
    | head::tail -> if (e <> head) then memberof e tail else true


let rec remove(item, lst) = 
    match lst with 
    | [] -> []
    | x::xs -> if (item <> x) then x::remove(item, xs) 
                else remove(item, xs)



(* Question 3. *)  (* Do not edit this line. *)
let findMax l = 
  let rec helper(l,m) = 
    match l with
    |[]-> m
    |head::tail -> if (head<m) then helper(tail, m) else helper(tail, head)
  match l with
  | [] -> failwith "Error -- empty list"
  | (x::xs) -> helper(xs,x)
  


(* Question 4. *)  (* Do not edit this line. *)
  
let rec selsort l =
  match l with
  |[] -> []
  |[x]->[x]
  |head::tail as list ->
    let max = findMax list
    if max = findMax (remove (max, list)) then
        selsort(remove(max, list))
      else
        max::selsort(head::remove (max, tail))

(* Question 5. *)  (* Do not edit this line. *)

let rec common twolists =
  match twolists with
  | ([], []) -> []
  | (head::tail, []) -> []
  | ([], head::tail) -> []
  | (x::xs, y::ys) ->
    if ((x = y) || (memberof x ys)) then x::  common (xs, y::ys) 
    else common (xs, y::ys) 

(* Question 6. *)   (* Do not edit this line. *)

(* Mergesort requires that you use recursion.  Using isort or
some other sort defeats the whole purpose.*)

let rec split l =
  match l with
    | [] -> ([],[])
    | [x] -> ([x], [])
    | x::y::xs ->
       let (leftList, rightList) = split xs in (x::leftList, y::rightList)


let rec merge twolists = 
 match twolists with
  | ([],[]) -> []
  | (l,[]) -> l
  | ([], l) -> l
  | (x::xs, y::ys) ->
    if x < y then
      x::y::merge(ys,xs)
    else
      y::x::merge(xs,ys)

let rec mergesort l = 
  match l with
  | [] -> []
  | (n::[]) -> n::[] (* Without this you will go into an infinite loop. *)
  | n::ns -> 
    let (ll, rl) = split l in merge (mergesort ll, mergesort rl)
    
  
