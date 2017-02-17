module Hw2

(* Question 1 *) 

let deriv (f, dx: float) = fun x -> ((f(x + dx) - f(x))/dx)


//  x(n+1) = x(n) - f(xn) / deriv f(xn)
let rec newton(f,guess:float,tol:float,dx:float) =
    if  ( (abs (f guess) )< tol ) then guess
    else  newton(f, (guess - ( f guess / deriv(f,dx) guess )),tol,dx)
  

//let make_cubic(a:float,b,c) = fun x -> (x*x*x + a * x*x + b*x + c)
//let a = newton(make_cubic(2.0,-3.0,1.0),0.0,0.0001,0.0001)

let root = newton(sin,5.0,0.0001,0.0001)


(* Question 2 *)

type term = Term of float * int
type poly = Poly of (float * int) list

exception EmptyList

let multiplyPolyByTerm(Term (c,e):term, Poly p:poly):poly = 
  (List.map(fun (a , b) -> a * c, b + e) p) |> Poly


let addTermToPoly(Term (c,e):term, Poly p:poly):poly = 
  let rec helper( c:float,e:int, l:((float * int)) list ) =
    match l with
    | [] -> raise EmptyList
    |[d] -> match d with 
            |(a,b)-> if(b=e) then [(a+c,b)]
                     elif (b<e) then [(c,e);(a,b)]
                     else [(a,b);(c,e)]
    |d::tail-> match d with
                | (a:float,b:int)->  if(b=e) then [(a+c,b)]@tail
                                     elif (e>b) then [(c,e)]@[(a,b)]@tail
                                     else [(a,b)]@ helper(c,e, tail)

  Poly (helper(c, e, p))



let addPolys(Poly p1:poly, Poly p2:poly):poly = 
    let rec helper(p1, p2)=
        match p1, p2 with
        | ([],[]) -> []
        | ([],x::xs) -> p2
        | (x::xs, []) -> p1
        | (x::xs, y::ys) -> match x, y with
                            | ((a,b),(c,d)) -> if (b=d) then [(a+c,d)]@helper(xs,ys)
                                               elif (b>d) then [(a,b)] @ helper (xs, p2)
                                               else  [(c,d)] @ helper (ys, p1)
    Poly (helper(p1, p2))                                              
     

let rec multPolys(Poly p1:poly, Poly p2:poly) = 
  match p1 with
  | [] -> raise EmptyList
  | [x]-> multiplyPolyByTerm(Term x, Poly p2) 
  | (x::xs) -> addPolys (multiplyPolyByTerm(Term x, Poly p2), multPolys (Poly xs, Poly p2))


let exp(b:float, e:int) =
  let rec helper(b:float, e:int, a: float) =
    if (b = 0.0) then 0.0
    elif (e = 0) then a
    elif (e % 2 = 1) then helper(b,e-1, b*a)
    else helper(b*b,e/2,a) 
  helper(b,e,1.0)

let evalTerm ((v:float), (Term (c,e):term)) = if (e=0) then c else c * exp(v,e)

let rec evalPoly(Poly p:poly,v:float):float = 
  match p with
  | [] -> raise EmptyList
  | [(a,b)] -> evalTerm (v, Term(a,b))
  | (a,b)::xs -> (evalTerm (v, Term(a,b)) + evalPoly(Poly xs, v ))   

// cx^n -> (c*n) x^(n-1)
let rec diffPoly (Poly p:poly ) = 
  match p with
  | [] -> raise EmptyList
  | [(a,b)] -> if (b=0) then Poly [(0.0 ,0)]
               else Poly [(a*(float)b, b-1)]
  | (a,b)::xs -> addTermToPoly( Term(a*(float)b, b-1), diffPoly (Poly xs) )




(* Question 3 *)
type Exptree =
  | Const of int 
  | Var of string 
  | Add of Exptree * Exptree 
  | Mul of Exptree * Exptree

type Bindings = (string * int) list

(* exception notFound *)

let rec lookup(name:string, env: Bindings) = 
  match env with
  | [] -> None
  | (a,b)::xs -> if (name = a) then Some(b)
                 else lookup(name, xs)
 

let rec insert(name:string, value: int, b: Bindings) = 
  match b with
  | [] -> [(name, value)]
  | (a,b)::xs -> if (name <= a) then [(name, value)]@(a,b)::xs
                 else insert(name, value, xs)
                                           
let rec eval(exp : Exptree, env:Bindings) = 
  match exp with
  | Const a -> Some(a)
  | Var a -> lookup(a, env)
  | Mul(a,b) -> match eval(a, env) with
               | None -> None
               | Some(a) ->  match eval(b, env) with
                             | None -> None
                             | Some(b) -> Some(a+b)

  | Add(a,b) -> match eval(a, env) with
                | None -> None
                | Some(a) ->  match eval(b, env) with
                              | None -> None
                              | Some(b) -> Some(a+b)


(* For testing 

let env:Bindings = [("a",3);("b",4);("c",5)]                                

let exp1 = Add(Const 3, Const 4)
let exp2 = Add(Const 3, Var "b")
let exp3 = Add(Var "c", Var "b")
let exp4 = Mul(exp3,exp2)
let exp5 = Add(Var "d",exp3)
let env2 = insert("b",10,env)

*)


(* Question 4 *)

type Team = string
type Goals = Goals of int
type Points = Points of int
type Fixture = Team * Team  
type Result = ((Team * Goals) * (Team * Goals))
type Table = Map<Team,Points>
    
let league =
  ["Chelsea"; "Spurs"; "Liverpool"; "ManCity"; "ManUnited"; "Arsenal"; "Everton"; "Leicester"]

let pointsMade (r: Result) = 
  match r with
  | ((t1, Goals g1),(t2, Goals g2)) -> if (g1 = g2) then ((t1, Points 1),(t2,Points 1))
                                       elif (g1 > g2) then ((t1,Points 3),(t2,Points 0))
                                       else ((t1,Points  0),(t2,Points 3))

let initEntry (name:Team) = (name, Points 0)
           
let initializeTable l = Map.ofList (List.map initEntry l)

let weekend1:Result list = [(("Chelsea", Goals 2),("Spurs", Goals 1)); (("Liverpool", Goals 3),("ManCity", Goals 2));(("ManUnited", Goals 1),("Arsenal", Goals 4));(("Everton", Goals 1),("Leicester", Goals 5))]

let weekend2:Result list = [(("Chelsea", Goals 5),("Arsenal", Goals 0)); (("Spurs", Goals 3),("ManCity",Goals 2)); (("ManUnited", Goals 1),("Liverpool", Goals 0));(("Everton", Goals 3),("Leicester", Goals 5))]

let s = [weekend2;weekend1]

let t0 = initializeTable league


let updateTable(t:Table,r:Result):Table = 
  let updateTableByTeam (r:Table, t1:Team,Points p1:Points)=
      if r.ContainsKey(t1) then 
        match Map.find t1 r with
            | Points a -> r.Add(t1, Points (p1+a))
      else
        r.Add(t1, Points (p1))
  match pointsMade r with
    |((t1, p1),(t2,p2)) -> updateTableByTeam( updateTableByTeam(t, t1, p1) , t2, p2)                                  

let rec weekendUpdate(t:Table,rl: Result list): Table =  
  match rl with
    | [] -> t
    | [x] -> updateTable(t, x)
    | x::xs -> weekendUpdate ( updateTable(t, x), xs)

let rec seasonUpdate(t:Table, sll:Result list list) : Table = 
  match sll with
    | [] -> t
    | [x] -> weekendUpdate(t, x)
    | x::xs -> seasonUpdate(weekendUpdate(t, x), xs)


let less((s1,n1):Team * Points, (s2,n2):Team * Points) =  
  (n1 < n2)

let rec myinsert item lst =
  match lst with
  | [] -> [item]
  | x::xs -> if less(item,x) then x::(myinsert item xs) else item::lst

let rec isort lst =
  match lst with
  | [] -> []
  | x::xs -> myinsert x (isort xs)

                                                  
(* Question 5 *)

type Destination = City of string
type RoadMap = Roads of Map<Destination, Set<Destination>>

let roadData = [
  "Andulo", ["Bibala"; "Cacolo"; "Dondo"]
  "Bibala", ["Andulo"; "Dondo"; "Galo"]
  "Cacolo", ["Andulo"; "Dondo"]
  "Dondo",  ["Andulo"; "Bibala"; "Cacolo"; "Ekunha"; "Funda"]
  "Ekunha", ["Dondo"; "Funda"]
  "Funda",  ["Dondo"; "Ekunha"; "Galo"; "Kuito"]
  "Galo",   ["Bibala"; "Funda"; "Huambo"; "Jamba"]
  "Huambo", ["Galo"]
  "Jamba",  ["Galo"]
  "Kuito",  ["Ekunha"; "Funda"]
]

let rec roadMap data = 
  Map.ofList((List.map(fun (a,b) -> City a, List.map City b |> Set.ofList) data )) |> Roads

let rec upToManySteps (Roads r) n startCity = 
  let rec helper(l) =
    match Set.toList l with
    | [] -> Set.empty<Destination>
    |x::xs -> Set.union (x) (helper (Set.ofList xs))

  match n with
    | 0 -> Set.empty
    | 1 -> Map.find startCity r
    | _ -> 
     helper (Set.map(fun x -> (upToManySteps (Roads r)(n-1) x)) (Map.find(startCity) r))
