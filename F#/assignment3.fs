 //module Hw3

(* Assignment 3 *) (* Do not edit this line. *)
(* Student name: Sebastian Andrade, Id Number: 260513637 *) (* Edit this line. *)

(* In the template below we have written the names of the functions that
you need to define.  You MUST use these names.  If you introduce auxiliary
functions you can name them as you like, except that your names should not
clash with any of the names we are using.  We have also shown the types
that you should have.  It is OK to change a "rec" declaration and put the
recursive function inside a helper if you want to.  Your code MUST compile
and must NOT go into infinite loops. *)

(* Question 1 *)
type Cell = { data : int; next : RList}
and RList = Cell option ref

let c1 = {data = 1; next = ref None}
let c2 = {data = 2; next = ref (Some c1)}
let c3 = {data = 3; next = ref (Some c2)}
let c5 = {data = 5; next = ref (Some c3)}

(* This converts an RList to an ordinary list, which is then displayed. *)
let rec displayList (c : RList) =
  match !c with
    | None -> []
    | Some { data = d; next = l } -> d :: (displayList l)

let cellToRList (c:Cell):RList = ref (Some c)

(* This is what you need to code. *)

let lista = cellToRList c5

let reverse (lst: RList) = 
  let rec swap(tail, head) = 
    match head with 
    | None -> lst := tail
    | Some c -> swap(head  ,!c.next ); c.next := tail                                
  swap(None, !lst); lst

(* Question 2*)

type transaction = Withdraw of int | Deposit of int | CheckBalance | ChangePassword of string | Close


(* Very limited bank account, one can only withdraw. *)

(* Datatype of bank account transactions.*)

(* Bank account generator. *)


let makeProtectedAccount(openingBalance: int, password: string) = 
  let balance = ref openingBalance 
  let pw = ref password
  let status = ref true
  let auth pass = (pass  = pw.contents)

  fun(pwTr:(string * transaction)) ->
  match pwTr with
  | (p, t) -> if ( auth p) then 
                  match t with 
                  | Withdraw(m) ->  if (!balance > m) 
                                          then
                                            balance := !balance - m
                                            printfn "Balance is %i" !balance
                                          else
                                            printfn "Insufficient funds."
                  | Deposit(m) -> (balance := !balance + m; (printf "Balance is %i\n" !balance))
                  | CheckBalance -> (printf "Balance is %i\n" !balance)
                  | ChangePassword(newPw) ->  pw := newPw
                                              printf "Password changed"
                  | Close -> status := false
                             printf "Account succesfully closed"                           
                else
                printf "Incorrect password, you fool!"


(* Question 3 *)

open System.Collections.Generic;;

type ListTree<'a> = Node of 'a * (ListTree<'a> list)

let bfIter f ltr = 
  let q = Queue<ListTree<'a>>()
  q.Enqueue ltr
  while (q.Count > 0) do
    let tail = q.Dequeue()
    match tail with 
    | Node (a,b) -> f a 
                    for n in b do q.Enqueue(n)

(* Some examples I used for testing.  *)
let n5 = Node(5,[])
let n6 = Node(6,[])
let n7 = Node(7,[])
let n8 = Node(8,[])
let n9 = Node(9,[])
let n10 = Node(10,[])
let n12 = Node(12,[])
let n11 = Node(11,[n12])
let n2 = Node(2,[n5;n6;n7;n8])
let n3 = Node(3,[n9;n10])
let n4 = Node(4,[n11])
let n1 = Node(1,[n2;n3;n4])

(* Just for testing, not needed for your solution. *)
let showNode n =
  match n with
    | Node(i,_) -> (printfn "%i" i)

    
bfIter (fun n -> printfn "%i" n) n1
