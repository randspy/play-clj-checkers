(ns checkers.board-spec
  (:use checkers.board)
  (:require [speclj.core :refer :all]))

(describe "Is clicked entity at the position."
             (it "Is at not the position."
                 (should-not (entity-at-position? {:height 55, :width 55, :y 430, :x 225} {:x 50 :y 50})))
             (it "Is at the position."
                 (should (entity-at-position? {:height 55, :width 55, :y 430, :x 225} {:x 260 :y 440}))))
