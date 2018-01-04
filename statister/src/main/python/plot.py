import matplotlib.pyplot as plt
import numpy as np

fname = "C:\Java\webstatistics\statister\stats\-world-20171225-1511627278.html-referrer_block=index_archive_1.stats"

with open(fname) as f:
    content = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line
content = [x.strip() for x in content]

content = content[6:]

valueArray = [[]]

for c in content:
#    print(c)
    values = c.split(" ")
    if valueArray.__len__() < values.__len__():
        valueArray = [[0 for x in range(values.__len__())] for y in range(values.__len__())]
    for i in range(values.__len__()):
        if i > 1:
            valueArray[i-1].append(int(values[i]))


print(valueArray)


valueBuffer = [[0 for x in range(0)] for y in range(valueArray.__len__() + 1)]

builds = np.array(range(valueArray[1].__len__() // 10))
#y_stack = np.zeros((valueArray.__len__(), valueArray[0].__len__() // 10))


#print(range(valueArray.__len__()))
for j in range(valueArray.__len__()):
    #   print(range(valueArray[j].__len__() // 10))
    for i in range(valueArray[j].__len__() // 10):
        if j == 1:
            valueBuffer[j].append(valueArray[j][i * 10])
            if valueArray[3][i * 10] != 0:
                valueBuffer[valueArray.__len__()].append(valueArray[2][i * 10] / valueArray[3][i * 10])
        else:
            valueBuffer[j].append(valueArray[j][i * 10])
#        y_stack.put(j, i, valueArray[j][i * 10])

while valueBuffer[valueArray.__len__()].__len__() < valueArray[1].__len__() // 10:
    valueBuffer[valueArray.__len__()].insert(0, 0)

y_stack = np.array(valueBuffer)
print(y_stack)


index1 = 0
fig = plt.figure(figsize=(12, 8))
ax1 = fig.add_subplot(111)

ax1.plot(builds, y_stack[index1], label="likes", color='c')

plt.xticks(builds)
plt.xlabel('time')

handles, labels = ax1.get_legend_handles_labels()
lgd = ax1.legend(handles, labels, loc='upper center', bbox_to_anchor=(1, 1))
ax1.grid('on')

# ax2 = fig.add_subplot(111)
#
# ax2.plot(builds, y_stack[1], label='Component 1', color='c')
#
# handles, labels = ax2.get_legend_handles_labels()
# lgd = ax2.legend(handles, labels, loc='upper center', bbox_to_anchor=(1, 1))
# ax2.grid('on')

plt.show()
