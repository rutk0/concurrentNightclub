# concurrentNightclub
Simulation showing behaviour of people in night club based on semaphores and threads

entrance bottleneck - max. 1 person can enter/leave club at the same momment
fight for resources - pints of beer for club guests are spawning randomly and every guest of club wants to drink it
if guest is drunk (>=3 beers) security guards kick him off of the club
