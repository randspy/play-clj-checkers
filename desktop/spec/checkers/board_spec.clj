(ns checkers.board-spec
  (:use checkers.board)
  (:require [speclj.core :refer :all]
            [clojure.core.matrix :as mx]))

(describe "Is clicked entity at the position."
          (it "Is at not the position."
              (should-not (entity-at-position? {:height 55, :width 55, :y 430, :x 225} {:x 50 :y 50})))
          (it "Is at the position."
              (should (entity-at-position? {:height 55, :width 55, :y 430, :x 225} {:x 260 :y 440}))))

(describe "Is selected pawn valid for a player."
     (describe "Is valid."
               (it "Pawn type matches player."
                   (should (selected-pawn-is-valid? [{:height 55, :width 55, :y-board-position 0,
                                                      :y 45, :x-board-position 1, :x 500}]
                                                    {:x 510 :y 50}
                                                    :white-player
                                                    (mx/matrix [[       0 0]
                                                                [:white-p 0]])))))
     (describe "Is not valid."
               (it "Pawn type does not match player."
                   (should-not (selected-pawn-is-valid? [{:height 55, :width 55, :y-board-position 0,
                                                          :y 45, :x-board-position 1, :x 500}]
                                                        {:x 510 :y 50}
                                                        :white-player
                                                        (mx/matrix [[       0 0]
                                                                    [:black-p 0]]))))))

(describe "Get pawn's position on the board."
          (it "Position present."
              (should= [1 1] (get-pawn-positon-on-board
                                [{:height 55 :width 55 :x 225 :y 430 :x-board-position 1 :y-board-position 1}]
                                {:x 260 :y 440}))))
