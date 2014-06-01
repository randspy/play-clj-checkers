(ns checkers.move-validation
  (:require [clojure.core.matrix :as mx]))


(defn- are-coordinates-positive [coordinates]
  (not (some #(< % 0) coordinates)))

(defn- are-coordinates-smaller-than-dimensions [dimensions coordinates]
  (not (some #(< % 1)
              (map - dimensions coordinates))))

(defn- is-inside-board [{:keys [board from to]}]
  (and
   (are-coordinates-smaller-than-dimensions (mx/shape board) from)
   (are-coordinates-smaller-than-dimensions (mx/shape board) to)
   (are-coordinates-positive from)
   (are-coordinates-positive to)))

(defn- is-destination-field-empty [{:keys [board to]}]
  (if (= 0 (mx/mget board (first to) (second to))) true false))

(defn is-pawn-on-coordinates [{:keys [board from]}]
  (not= 0 (mx/mget board (first from) (second from))))

(defn is-move-valid [move]
  (and
    (is-inside-board move)
    (is-pawn-on-coordinates move)
    (is-destination-field-empty move)))
