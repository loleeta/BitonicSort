# BitonicSort
Bitonic sorter pipelining program in Java, which takes in a stream of arrays of floating point numbers (Double in Java), and outpus the arrays in sorted order, using bitonic sort. A sequential sorting net is used as a benchmark for parallelization. The pipeline conists of seven threads for sorting, using SynchronousQueue objects to handle pipelined data. 


The program was tested with various sizes of arrays and yielded the following results
<img src="https://user-images.githubusercontent.com/3770476/47273724-554bda80-d54e-11e8-9bfc-f5ec959ed7be.png" width="50%">
