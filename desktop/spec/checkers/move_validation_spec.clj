(ns checkers.move-validation-spec
  (:use checkers.move-validation)
  (:require [speclj.core :refer :all]
            [clojure.core.matrix :as mx]))


(describe "moves validation"
  (describe "move to empty field"
    (it "can move"
        (should (is-move-valid
                 {:board (mx/matrix [[       0 0]
                                     [:white-p 0]]) :from [1 0] :to [0 1]})))

    (it "can't move because place is alredy occupied"
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 :white-p]
                                         [:white-p        0]]) :from [1 0] :to [0 1]}))))

    (it "can't move because selected pown does not exist"
        (should-not (is-move-valid {:board (mx/matrix [[0 0] [0 0]]) :from [1 0] :to [0 1]})))

  (describe "moves outside of the board"
    (it "can't move in coordinates over board size"
        (should-not (is-move-valid
                     {:board (mx/matrix [[0        0]
                                         [0 :white-p]]) :from [1 1] :to [2 2]})))

    (it "can't move into negative coordinates"
        (should-not (is-move-valid
                     {:board (mx/matrix [[:white-p 0]
                                         [       0 0]]) :from [0 0] :to [-1 -1]})))

    (it "can't move in coordinates over board size"
        (should-not (is-move-valid
                     {:board (mx/matrix [[0        0]
                                         [0 :white-p]]) :from [2 1] :to [1 0]})))

    (it "can't move from negative coordinates"
        (should-not (is-move-valid
                     {:board (mx/matrix [[0 0]
                                         [0 0]]) :from [-1 0] :to [0 1]}))))
  (describe "white pawn moves"
    (it "can move up right"
        (should (is-move-valid
                 {:board (mx/matrix [[       0 0]
                                     [:white-p 0]]) :from [1 0] :to [0 1]})))
    (it "can move up left"
        (should (is-move-valid
                 {:board (mx/matrix [[0 0        0]
                                     [0 0        0]
                                     [0 0 :white-p]]) :from [2 2] :to [1 1]})))

    (it "can't move other plases than up left and up rights"
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0]
                                         [:white-p 0]]) :from [1 0] :to [0 0]}))

        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0]
                                         [:white-p 0]]) :from [1 0] :to [1 1]})))

    (it "can't move more than one field"
        (should-not (is-move-valid
                     {:board (mx/matrix [[       0 0 0 0]
                                         [       0 0 0 0]
                                         [       0 0 0 0]
                                         [:white-p 0 0 0]]) :from [3 0] :to [0 3]})))
    (it "can beat black pawn"
        (should (is-move-valid
                 {:board (mx/matrix [[       0        0 0]
                                     [       0 :black-p 0]
                                     [:white-p        0 0]]) :from [2 0] :to [0 2]}))))
    (it "can't beat white pawn"
        (should-not (is-move-valid
                   {:board (mx/matrix [[       0        0 0]
                                       [       0 :white-p 0]
                                       [:white-p        0 0]]) :from [2 0] :to [0 2]}))

        (should-not (is-move-valid
                   {:board (mx/matrix [[       0        0 0]
                                       [       0 :white-q 0]
                                       [:white-p        0 0]]) :from [2 0] :to [0 2]})))

  (describe "black pawn moves"
    (it "can move down right"
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :black-p 0]
                                     [0        0 0]]) :from [1 1] :to [2 2]})))
    (it "can beat white pawn"
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 :black-p]
                                     [0 :white-p        0]
                                     [0        0        0]]) :from [0 2] :to [2 0]})))
    (it "can't move more than one field"
        (should-not (is-move-valid
                     {:board (mx/matrix [[0 0 0 :black-p]
                                         [0 0 0        0]
                                         [0 0 0        0]
                                         [0 0 0        0]]) :from [0 3] :to [3 0]}))))
  (describe "white queen moves"
    (it "can move down all directions"
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :white-q 0]
                                     [0        0 0]]) :from [1 1] :to [2 2]}))))
  (describe "black queen moves"
    (it "can move down all directions"
        (should (is-move-valid
                 {:board (mx/matrix [[0        0 0]
                                     [0 :black-q 0]
                                     [0        0 0]]) :from [1 1] :to [2 2]})))))
