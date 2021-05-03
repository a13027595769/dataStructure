package com.supremind.heap;

import com.supremind.map.asserta.printer.BinaryTreeInfo;

import java.util.Comparator;


/**
 * 二叉堆（最大堆）
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class BinaryHeap<E> implements Heap<E>, BinaryTreeInfo {
	private E[] elements;
	private int size;
	private Comparator<E> comparator;
	private static final int DEFAULT_CAPACITY = 10;
	public BinaryHeap(Comparator<E> comparator){
		this.comparator = comparator;
		this.elements = (E[])new Object[DEFAULT_CAPACITY];
	}
	public BinaryHeap(){
		this(null);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
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
		return null;
	}

	@Override
	public E replace(E element) {
		return null;
	}
	/*
	让index位置的元素上滤
	 */
	private void siftUp(int index){
		//这个就是你新添加的那个元素，这个元素是需要上滤的，所以只需要获取一遍就够了
		E e = elements[index];
		//index的元素大于0才说明有父节点，否则就是根节点了
		while (index > 0){
			//然后要和父节点比较，父节点的索引怎么算呢？floor((i - 1)/2)，然后java默认就是向下取整的
			//不用调用floor函数了，然后除以2，移位比较快
			int pIndex = (index - 1) >> 1;
			E p = elements[pIndex];
			//如果小于等于0，那就直接return好了，不用比较了，就跟红黑树添加节点一样，默认是红的，父节点黑的，正好
			if(compare(e,p) <= 0) return;
			//交换index，pIndex的位置
			E tmp = elements[index];
			elements[index] = elements[pIndex];
			elements[pIndex] = tmp;
			//每次循环完之后，要变索引的，因为可能会一直往上比较，交换完之后，pindex就要变成index重新往上比较
			//然后再次取整之类的，直到不满足条件，退出循环
			index = pIndex;

		}
	}

	private int compare(E e1,E e2){
		return comparator != null ? comparator.compare(e1, e2)
				: ((Comparable<E>)e1).compareTo(e2);
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
		Integer index = (Integer)node;
		index = (index << 1) + 1;
		return index >= size ? null : index;
	}

	@Override
	public Object right(Object node) {
		Integer index = (Integer)node;
		index = (index << 1) + 2;
		return index >= size ? null : index;
	}

	@Override
	public Object string(Object node) {
		Integer index = (Integer)node;
		return elements[index];
	}
}
