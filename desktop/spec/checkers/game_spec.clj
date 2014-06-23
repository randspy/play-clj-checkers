(ns checkers.game-spec
  (:use checkers.game)
  (:require [speclj.core :refer :all]
            [clojure.core.matrix :as mx]))


(describe "Game between two players."
  (describe "White player makas a move."
    (with board (atom (mx/matrix
                       [[:black-p        0 :black-p        0 :black-p]
                        [       0 :black-p        0 :black-p        0]
                        [       0        0        0        0        0]
                        [       0 :white-p        0 :white-p        0]
                        [:white-p        0 :white-p        0 :white-p]])))
    (it "White pawn goes up left."
        (should= (mx/matrix
                  [[:black-p        0 :black-p        0 :black-p]
                   [       0 :black-p        0 :black-p        0]
                   [:white-p        0        0        0        0]
                   [       0        0       0 :white-p        0]
                   [:white-p        0 :white-p        0 :white-p]])
                 (move-white {:board @@board :from [3 1] :to [2 0]})))))
