(define distance
  (lambda (x y)
    (sqrt (+ (* x x) (* y y)))))

(display "Distance: ")
(display (distance 3 4))
(newline)