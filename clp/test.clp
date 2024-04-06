(deffunction log-time ()
	(bind ?d (local-time))
    (format nil "%d/%d/%d %d:%d:%d"
      (nth$ 1 ?d) (nth$ 2 ?d) (nth$ 3 ?d)
      (nth$ 4 ?d) (nth$ 5 ?d) (nth$ 6 ?d)))
