(ns checkers.move-validation-spec
  (:use checkers.move-validation)
  (:require [speclj.core :refer :all]
            [clojure.core.matrix :as mx]))


(describe "moves validation"
          (describe "move to empty field"
                    (it "can move"
                        (should (is-move-valid
                                 {:board (mx/matrix [[0 0] [:wp 0]]) :from [1 0] :to [0 1]})))
                    (it "can't move because place is alredy occupied"
                        (should-not (is-move-valid
                                     {:board (mx/matrix [[0 :wp] [:wp 0]]) :from [1 0] :to [0 1]}))))
                    (it "can't move because selected pown does not exist"
                        (should-not (is-move-valid {:board (mx/matrix [[0 0] [0 0]]) :from [1 0] :to [0 1]})))

          (describe "moves outside of the board"
                    (it "can't move in coordinates over board size"
                        (should-not (is-move-valid
                                     {:board (mx/matrix [[0 0] [0 :wp]]) :from [1 1] :to [2 2]})))
                    (it "can't move into negative coordinates"
                        (should-not (is-move-valid
                                     {:board (mx/matrix [[:wp 0] [0 0]]) :from [0 0] :to [-1 -1]})))
                    (it "can't move in coordinates over board size"
                        (should-not (is-move-valid
                                     {:board (mx/matrix [[0 0] [0 :wp]]) :from [2 1] :to [1 0]})))
                    (it "can't move from negative coordinates"
                        (should-not (is-move-valid
                                     {:board (mx/matrix [[0 0] [0 0]]) :from [-1 0] :to [0 1]})))))
