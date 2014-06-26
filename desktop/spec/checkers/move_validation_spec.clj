(ns checkers.move-validation-spec
  (:use checkers.move-validation)
  (:require [speclj.core :refer :all]
            [clojure.core.matrix :as mx]))


(describe "Moves validation."
  (describe "Move to empty field."
    (it "Can move."
        (should (is-move-valid
                 {:board (mx/matrix [[       0 0]
                                     [:white-p 0]]) :from [1 0] :to [0 1] :pawn-type [:white-p]})))

    (it "Can't move because place is alredy occupied."
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 :white-p]
                                         [:white-p        0]]) :from [1 0] :to [0 1] :pawn-type [:white-p]}))))

    (it "Can't move because selected pown does not exist."
        (should-not (is-move-valid  {:board (mx/matrix [[0 0] [0 0]]) :from [1 0] :to [0 1] :pawn-type [:white-p]})))

  (describe "Moves outside of the board."
    (it "Can't move in coordinates over board size."
        (should-not (is-move-valid
                     {:board (mx/matrix [[0        0]
                                         [0 :white-p]]) :from [1 1] :to [2 2] :pawn-type [:white-p]})))

    (it "Can't move into negative coordinates."
        (should-not (is-move-valid
                     {:board (mx/matrix [[:white-p 0]
                                         [       0 0]]) :from [0 0] :to [-1 -1] :pawn-type [:white-p]})))

    (it "Can't move in coordinates over board size."
        (should-not (is-move-valid
                     {:board (mx/matrix [[0        0]
                                         [0 :white-p]]) :from [2 1] :to [1 0] :pawn-type [:white-p]})))

    (it "Can't move from negative coordinates."
        (should-not (is-move-valid
                     {:board (mx/matrix [[0 0]
                                         [0 0]]) :from [-1 0] :to [0 1] :pawn-type [0]}))))
  (describe "White pawn moves."
    (it "Can move up right."
        (should (is-move-valid
                 {:board (mx/matrix [[       0 0]
                                     [:white-p 0]]) :from [1 0] :to [0 1] :pawn-type [:white-p]})))
    (it "Can move up left."
        (should (is-move-valid
                 {:board (mx/matrix [[0 0        0]
                                     [0 0        0]
                                     [0 0 :white-p]]) :from [2 2] :to [1 1] :pawn-type [:white-p]})))

    (it "Can't move other plases than up left and up rights."
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0]
                                         [:white-p 0]]) :from [1 0] :to [0 0] :pawn-type [:white-p]}))

        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0]
                                         [:white-p 0]]) :from [1 0] :to [1 1] :pawn-type [:white-p]})))

    (it "Can't move more than one field."
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0 0 0]
                                         [       0 0 0 0]
                                         [       0 0 0 0]
                                         [:white-p 0 0 0]]) :from [3 0] :to [0 3] :pawn-type [:white-p]})))
    (it "Can beat black pawn."
        (should (is-move-valid
                 {:board (mx/matrix [[       0        0 0]
                                     [       0 :black-p 0]
                                     [:white-p        0 0]]) :from [2 0] :to [0 2] :pawn-type [:white-p]}))))
    (it "Can't beat white pawn."
        (should-not (is-move-valid
                   {:board (mx/matrix [[       0        0 0]
                                       [       0 :white-p 0]
                                       [:white-p        0 0]]) :from [2 0] :to [0 2] :pawn-type [:white-p]}))

        (should-not (is-move-valid
                   {:board (mx/matrix [[       0        0 0]
                                       [       0 :white-q 0]
                                       [:white-p        0 0]]) :from [2 0] :to [0 2] :pawn-type [:white-p]})))

    (it "Can't move black pown here."
        (should-not (is-move-valid
                 {:board (mx/matrix [[       0        0 0]
                                     [       0 :black-p 0]
                                     [:white-p        0 0]]) :from [1 1] :to [2 2] :pawn-type [:white-p]})))

  (describe "Black pawn moves."
    (it "Can move down right."
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :black-p 0]
                                     [0        0 0]]) :from [1 1] :to [2 2] :pawn-type [:black-p]})))
    (it "Can beat white pawn."
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 :black-p]
                                     [0 :white-p        0]
                                     [0        0        0]]) :from [0 2] :to [2 0] :pawn-type [:black-p]})))
    (it "Can't move more than one field."
        (should-not (is-move-valid
                     {:board (mx/matrix [[0 0 0 :black-p]
                                         [0 0 0        0]
                                         [0 0 0        0]
                                         [0 0 0        0]]) :from [0 3] :to [3 0] :pawn-type [:black-p]}))))
  (describe "White queen moves."
    (it "Can move down all directions."
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :white-q 0]
                                     [0        0 0]]) :from [1 1] :to [2 2] :pawn-type [:white-q]}))))
  (describe "Black queen moves."
    (it "Can move down all directions."
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :black-q 0]
                                     [0        0 0]]) :from [1 1] :to [2 2] :pawn-type [:black-q]})))))
