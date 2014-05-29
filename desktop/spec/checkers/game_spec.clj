(ns checkers.game-spec
  (:require [speclj.core :refer :all]))

(defn true-or-false []
  true)

(describe "truthiness"
  (it "tests if true-or-false returns true"
    (should (true-or-false))))



