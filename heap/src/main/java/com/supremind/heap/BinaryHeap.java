package com.supremind.heap;

import com.supremind.map.asserta.printer.BinaryTreeInfo;
import com.supremind.map.asserta.printer.BinaryTrees;

import java.util.Comparator;


/**
 * 二叉堆（最大堆）
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
	private E[] elements;

	private static final int DEFAULT_CAPACITY = 10;
	public BinaryHeap(E[] elements,Comparator<E> comparator){
		super(comparator);
		if(elements == null || elements.length == 0){
			this.elements = (E[])new Object[DEFAULT_CAPACITY];
		}else{
			//size一定要传，否则到下面下滤就会没有长度，
			size = elements.length;
			//this.elements = elements;这样是不行的，因为默认传的是引用，如果别人对数据再
			//做点什么变化，那你这就不是最大堆了，所以，要实现一个深拷贝
			int capacity = 	Math.max(elements.length,DEFAULT_CAPACITY);
			this.elements = (E[])new Object[capacity];
			for (int i = 0; i < elements.length; i++) {
				this.elements[i] = elements[i];
			}
			heapify();
		}
	}

	/**
	 * 批量建堆
	 */
	private void heapify() {
//		//自上而下的上滤，
//		for (int i = 0; i < elements.length; i++) {
//			siftUp(i);
//		}
		//自下而上的下滤，
		for (int i = (size >> 1) - 1; i >= 0; i--) {
			siftDown(i);
		}
	}

	public BinaryHeap(E[] elements){
		this(elements,null);
	}
	public BinaryHeap(Comparator<E> comparator){
		this.comparator = comparator;
		this.elements = (E[])new Object[DEFAULT_CAPACITY];
	}
	public BinaryHeap(){
		this(null,null);
	}


	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	@Override
	public void add(E element) {
		elementNotNullCheck(element);
		ensureCapacity(size + 1);
		elements[size++] = element;
		siftUp(size - 1);
	}

	@Override
	public E get() {
		emptyCheck();
		return elements[0];
	}

	@Override
	public E remove() {
		//删除堆顶元素
		emptyCheck();
		int lastIndex = --size;
		//先保存一个堆顶元素，因为你删除之后，你要返回回去的
		E root = elements[0];
		//最后一个元素先覆盖掉堆顶元素
		elements[0] = elements[lastIndex];
		//最后一个元素清空
		elements[lastIndex] = null;
//		size--;
		siftDown(0);
		return root;
	}

	@Override
	public E replace(E element) {
		/*
		E root = remove();
		add(element);
		return root;
		这样倒是行，但是，remove和add都是logn级别的，remove会下滤，先把堆顶删除，再把最后面的覆盖，再
		一个一个的比较，没必要，直接把新添加覆盖掉堆顶元素就好了。
		 */
		elementNotNullCheck(element);
		E root = null;
		if(size == 0){
			elements[0] = element;
			size++;
		}else{
			root = elements[0];
			elements[0] = element;
			siftDown(0);
		}
		return root;
	}
	private void siftDown(int index){
		E element = elements[index];
		int half = size >> 1; // 非叶子节点的数量直接除以2
		//第一个叶子节点的数量 == 非叶子节点的数量
		/*
		叶子节点的数量 no = floor((n + 1) / 2) = ceiling(n / 2)
		非叶子节点的数量 no = floor(n / 2) = ceiling((n-1) / 2)
		 */
		//比如5,是第一个叶子节点，那1到4肯定是非叶子节点啊，毕竟5是第一个叶子节点
		//while(index < 第一个叶子节点的索引)//必须保证index位置是非叶子节点
		//叶子节点的个数 n0== floor((n + 1)/2) = ceiling(n/2)
		//非叶子节点的数量 n1 + n2= floor(n / 2) =ceiling((n-1)/2)
		while(index < half){
			//index的节点有两种情况，
			//1只有左子节点
			//2同时有左右子节点
			//取谁呢？肯定取左边啊，因为右边有可能不存在啊。
			/*
			如果 i = 0 ，它是根节点
			如果 i > 0 ，它的父节点的索引为 floor( (i – 1) / 2 )

			如果 2i + 1 ≤ n – 1，它的左子节点的索引为 2i + 1
			如果 2i + 1 > n – 1 ，它无左子节点

			如果 2i + 2 ≤ n – 1 ，它的右子节点的索引为 2i + 2
			如果 2i + 2 > n – 1 ，它无右子节点
			 */
			//默认为左子节点跟它比较
			int childIndex = (index << 1) + 1;
			E child = elements[childIndex];
			//右子节点，不用套公式了，右子节点不就是左子节点加一么
			int rightIndex = childIndex + 1;
			//选出左右子节点最大的那个
			/*
			这里childIndex肯定存在，+ 1肯定也是个整数，不存在说为null一说，这里就是看是不是越界了，看看是不是
			在数组的size之内
			然后进行比较，不能是大于等于0，大于等于0，就一样嘛，就不覆盖了，大于0才说明要覆盖
			 */
			if(rightIndex < size && compare(elements[rightIndex],child) > 0){
				//右边的比左边的大，右边的覆盖左边的，因为默认是左边的，现在就是右边比左边大的情况。
				childIndex = rightIndex;
				child = elements[rightIndex];
			//	child = elements[childIndex = rightIndex];  装逼写法，还是算了，可读性差，
				//因为是右边赋值给坐班，那其实俩都是一样的，都是右边的那个，直接放里面也行。
			}

			//如果大于0，说明已经满足最大堆的性质了，可以退出循环了
			if(compare(element,child) >= 0)break;
			//把子节点存到index位置,index什么位置？堆顶，
			elements[index] = child;
			//childIndex现在是堆顶的左子节点，再把左子节点赋值给index,现在他俩的值是一样的
			// 以左子节点为参照系，现在，左子节点是根节点，然后循环往下走，一直比，直到那个break跳出来，
			//才进行真正的赋值，之前一直是element和child比较，element在外面声明的，没动过。
			index = childIndex;

		}
		elements[index] = element;
		BinaryTrees.println(this);
	}
	/*
	让index位置的元素上滤
	 */
	private void siftUp(int index){
		//这个就是你新添加的那个元素，这个元素是需要上滤的，所以只需要获取一遍就够了
		E element = elements[index];
		//index的元素大于0才说明有父节点，否则就是根节点了
		while (index > 0){
			//然后要和父节点比较，父节点的索引怎么算呢？floor((i - 1)/2)，然后java默认就是向下取整的
			//不用调用floor函数了，然后除以2，移位比较快
			int parentIndex = (index - 1) >> 1;

			E parent = elements[parentIndex];
			//如果小于等于0，那就直接return好了，不用比较了，就跟红黑树添加节点一样，默认是红的，父节点黑的，正好
			//现在变成了break，因为return直接就结束所有循环了，这个要在后面吧原来的给赋值，所以改成break；
			if(compare(element,parent) <= 0) break;
			//交换index，pIndex的位置，先把父节点赋值给node，首先前面的要满足，新添加的必须比父节点大，
			//然后node再往上面的父节点进行比较，直到最后，然后再把添加的这个给覆盖
			//这个index还是最大的那个元素，因为一直是它在比较，现在变成父节点，一直再往上比较
			elements[index] = parent;
			//每次循环完之后，要变索引的，因为可能会一直往上比较，交换完之后，pindex就要变成index重新往上比较
			//然后再次取整之类的，直到不满足条件，退出循环,新添加的元素，index是一直在变的，是它一直在比较
			//否则，你怎么知道覆盖哪个？
			index = parentIndex;
		}
		/*
			    ┌─68─┐
			    │    │
			 ┌─68─┐  43
			 │    │
			50    38
			没有这行代码，就没法进行最终的覆盖了
			    ┌─72─┐
			    │    │
			 ┌─68─┐  43
			 │    │
			50    38
			最终的是这样，return改为break，因为return直接整个函数就结束了，break跳出while循环，还能向下循环
		 */
		elements[index] = element;
	}


	private void ensureCapacity(int capacity) {
		int oldCapacity = elements.length;
		//如果原来的比传入的参数大，就没必要动了，直接return
		if(oldCapacity >= capacity) return;

		int newCapacity = oldCapacity + (oldCapacity >> 1);
		E[] newElements = (E[]) new Object[newCapacity];
		for (int i = 0; i < size; i++) {
			newElements[i] = elements[i];
		}
		elements = newElements;
		System.out.println(oldCapacity+"扩容为"+newCapacity);
	}
	private void emptyCheck(){
		if(size == 0){
			throw new IndexOutOfBoundsException("Heap is empty");
		}
	}
	private void elementNotNullCheck(E element){
		if(element == null){
			throw new IllegalArgumentException("element must be not null");
		}
	}

	@Override
	public Object root() {
		return 0;
	}

	@Override
	public Object left(Object node) {
		int index = ((int)node << 1) + 1;
		return index >= size ? null : index;
	}

	@Override
	public Object right(Object node) {
		int index = ((int)node << 1) + 2;
		return index >= size ? null : index;
	}

	@Override
	public Object string(Object node) {
		return elements[(int)node];
	}
}
