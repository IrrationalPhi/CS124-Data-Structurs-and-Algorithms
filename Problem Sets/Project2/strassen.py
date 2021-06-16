import math
import random
from time import perf_counter
from operator import add, sub
# import matplotlib.pyplot as plt
import sys

crossover = 50

# let's do dumb implementation first
def traditional_multiply(a, b):
    n = len(a)
    res = [[0 for i in range(n)] for j in range(n)]
    for k in range(n):
        for i in range(n):
            temp = a[i][k]
            for j in range(n):
                res[i][j] += temp * b[k][j]
    
    return res

def plus(a, b):
    n = len(a)
    res = []
    for i in range(len(a)):
        res.append(list(map(add, a[i], b[i])))
        
    return res

def subtract(a, b):
    n = len(a)
    res = []
    for i in range(len(a)):
        res.append(list(map(sub, a[i], b[i])))
    return res

def split(matrix):
    n = len(matrix)
    half = math.ceil(n/2)
    splits = ([], [], [], [])
    
    for i in range(half):
        splits[0].append(matrix[i][:half])
        splits[1].append(matrix[i][half:])
        
    for i in range(half, n):
        splits[2].append(matrix[i][:half])
        splits[3].append(matrix[i][half:])
        
    if n % 2:
        # pad
        splits[2].append([0] * half)
        splits[3].append([0] * (half - 1))
        
        for i in range(half):
            splits[1][i].append(0)
            splits[3][i].append(0)
    
    return splits

def strassen_multiply(a, b):
    n = len(a)
    
    if n == 1:
        return [[a[0][0] * b[0][0]]]
    
    if n < crossover:
        return traditional_multiply(a, b)
    
    As = split(a)
    Bs = split(b)
    
    prods = [
        strassen_multiply(plus(As[0], As[3]), plus(Bs[0], Bs[3])), # A+D times E+H
        strassen_multiply(plus(As[2], As[3]), Bs[0]), # C+D times E
        strassen_multiply(As[0], subtract(Bs[1], Bs[3])), # A times F-H
        strassen_multiply(As[3], subtract(Bs[2], Bs[0])), #D times G-E
        strassen_multiply(plus(As[0], As[1]), Bs[3]), #A+B times H
        strassen_multiply(subtract(As[2], As[0]), plus(Bs[0], Bs[1])), #C-A times E+F
        strassen_multiply(subtract(As[1], As[3]), plus(Bs[2], Bs[3])) #B-D times G+H
    ]
    
    subresults = [
        plus(plus(prods[0], prods[3]), subtract(prods[6], prods[4])),
        plus(prods[2], prods[4]),
        plus(prods[1], prods[3]),
        plus(plus(prods[0], prods[2]), subtract(prods[5], prods[1]))
    ]
    
    res = []
    for i in range(len(subresults[0])):
        res.append(subresults[0][i] + subresults[1][i])
    
    for i in range(len(subresults[2])):
        res.append(subresults[2][i] + subresults[3][i])
        
    # unpad
    if n % 2:
        del res[-1]
        for r in res:
            del r[-1]
    
    return res

def generate_matrix(size):
    res = []
    for i in range(size):
        res.append([random.randint(0, 64) for _ in range(size)])
    return res

def main():
    argv = sys.argv
    size = int(argv[2])
    input_file = argv[3]
    
    crossover = 50
    
    a = [[0 for i in range(size)] for j in range(size)]
    b = [[0 for i in range(size)] for j in range(size)]
    
    with open(input_file) as f:
        lines = f.read().splitlines()
        nums = [int(_) for _ in lines]
        
        for i in range(size):
            a[i] = nums[0:size]
            del nums[0:size]
        for i in range(size):
            b[i] = nums[0:size]
            del nums[0:size]
        
        res = strassen_multiply(a, b)
        for i in range(size):
            print(res[i][i])
        
    return
        
    
if __name__ == "__main__":
    main()

# ### LOOK FOR EXPERIMENTAL CROSSOVER
# ### for even, it's 32
# ### for odd, it's 51

# strassen_means = []
# traditional_means = []
# num_reps = 15

# for n in range(1, 200):
#     # avg strassen speed on nxn matrix multiplication
#     strassen_mean = 0
#     traditional_mean = 0
#     crossover = n
    
#     for i in range(num_reps):
#         a = generate_matrix(n+1)
#         b = generate_matrix(n+1)
        
#         # testing traditional
#         start = perf_counter()
#         temp = traditional_multiply(a, b)
#         traditional_mean += perf_counter() - start

#         # testing strassen
#         start = perf_counter()
#         temp = strassen_multiply(a, b)
#         strassen_mean += perf_counter() - start

#     strassen_mean /= num_reps
#     traditional_mean /= num_reps
    
#     strassen_means.append(strassen_mean)
#     traditional_means.append(traditional_mean)
#     print(f"step {n}: done")

# crossover = 50


# ### PLOTS
# crossovers = [k+1 for k in range(199)]
# plt.plot(crossovers, strassen_means, label = "strassen")
# plt.plot(crossovers, traditional_means, label = "traditional")
# plt.title("comparison of strassen and traditional times: 15 trials")
# plt.xlabel("crossover value")
# plt.ylabel("average run time")
# plt.legend(loc = 'best')

# differences = [traditional_means[i] - strassen_means[i] for i in range(len(traditional_means))]
# crossovers = [k+1 for k in range(199)]
# plt.plot(crossovers, differences)
# plt.plot(crossovers, [0]*199)
# plt.title("difference of traditional and strassen times: 15 trials")
# plt.xlabel("crossover value")
# plt.ylabel("average run time (scaled)")
# plt.ylim([-0.5, 0.5])



# ### NOW WE WORK ON TRIANGLES

# size = 1024
# expectations = []
# num_reps = 100
# for p in {0.01, 0.02, 0.03, 0.04, 0.05}:
#     mean = 0
#     for rep in range(num_reps):
#         adj_matrix = [[0 for i in range(size)] for j in range(size)]
#         for i in range(size):
#             for j in range(i):
#                 if random.random() < p:
#                     adj_matrix[i][j] = 1
#                     adj_matrix[j][i] = 1

#         temp = strassen_multiply(strassen_multiply(adj_matrix, adj_matrix), adj_matrix)
#         res = 0
#         for i in range(size):
#             res += temp[i][i]
#         res /= 6
#         mean += res
#     mean /= num_reps
#     expectations.append(mean)