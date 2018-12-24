import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Enumerable<T> implements List<T>{
	private List<T> list;
	private T defaultValue = null;
	
	public Enumerable() {
		this.list = new ArrayList<T>();
	}
	
	public Enumerable(List<T> list) {
		this.list = list;
	}
	
	public Enumerable<T> skip(int numberToSkip) {
		return new Enumerable<T>(
				subList(numberToSkip, size()).toList());
	}	
	
	public Enumerable<T> skipWhile(EnumerableWhere<T> functionalInterface) {
		Enumerable<T> result = new Enumerable<>(list);				
		for(int i = 0; i < size(); i++) {
			if(!functionalInterface.where(get(i))) {
				return subList(i, size());                
			}
		}        
		return result;
	}	
	
	public Enumerable<T> take(int numberToTake) {
		return new Enumerable<T>(subList(0, numberToTake).toList());
	}	
	
	public T orReturn(T defaultValue) {
		return isEmpty() ? defaultValue : get(0);
	}	
			
	public Enumerable<T> select(EnumerableWhere<T> functionalInterface) {
		Enumerable<T> result = new Enumerable<T>();
		for(T item: list) {
			if(functionalInterface.where(item)) {
				result.add(item);
			}
		}
		return result;
    }
	
	public Enumerable<T> where(EnumerableWhere<T> functionalInterface) {
		Enumerable<T> result = new Enumerable<T>();
		for(T item: list) {
            if(functionalInterface.where(item)) {
                result.add(item);
            }
        }
        return result;
    }
	
	public Enumerable<T> takeWhile(EnumerableWhere<T> functionalInterface) {
		Enumerable<T> result = new Enumerable<T>();        
		for(T item: list) {
			if(functionalInterface.where(item)) {
				result.add(item);
			}else{
				return result;
			}
		}       
		return result;
    }	
	
	public <S> Enumerable<T> ofType(Class<S> classType){
		Enumerable<T> result = new Enumerable<T>();
		for(T item: list) {
			if(item != null) {
	            if(classType.isAssignableFrom(item.getClass())) {
	            	result.add(item);
	            }
			}
        }
		return result;
	}
	
	public Enumerable<T> filter(EnumerableWhere<T> functionalInterface){
		return where(functionalInterface);
	}	
	
	public Enumerable<T> and(EnumerableWhere<T> functionalInterface){
		return where(functionalInterface);
	}	
	
	public Enumerable<T> thenBy(Comparator<? super T> c){
		return sortAndReturn(c);
	}
	
	public Enumerable<T> join(Enumerable<T> toJoin){
		addAll(toJoin);
		return this;
	}
	
	public Enumerable<T> union(Enumerable<T> toUnion){
		concat(toUnion);
		return this;
	}
	
	public Enumerable<T> intersect(Enumerable<T> toIntersect){
		Enumerable<T> result = new Enumerable<T>();
		for(T item: list) {
			if(toIntersect.contains(item)) {
				result.add(item);
			}
		}
		return result;
	}
	
	public boolean areDisjoint(Enumerable<T> toDisjoin){
		for(T item: list) {
			if(toDisjoin.contains(item)) {
				return false;
			}
		}
		
		for(T item: toDisjoin) {
			if(list.contains(item)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean sequenceEval(Enumerable<T> comparison) {
		if(size() != comparison.size()) {
			return false;
		}else{
			for(int i = 0; i < size(); i++) {
				if(!get(i).equals(comparison.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public T elementAtOrDefault(int index){
		if(index >= size()) {
			return defaultValue;
		}else{
			return get(index);
		}
	}
	
	public Enumerable<T> distinct() {
		Enumerable<T> result = new Enumerable<>();
		HashSet<T> distinctValues = new HashSet<T>(list);
		for(T item: distinctValues) {
			result.add(item);
		}
		return result;
	}
	
	public T single(EnumerableWhere<T> functionalInterface) {
		Enumerable<T> result = where(functionalInterface);
		if(result.size() != 1) {
			return null;
		}else{
			return result.get(0);
		}
	}
	
	public Enumerable<T> reverse(){
		Collections.reverse(list);
		return this;
	}	
	
	public void prepend(T element) {
		add(0, element);
	}
	
	public boolean append(T item) {
		return add(item);
	}
	
	public void concat(Enumerable<T> enumerable) {
		Iterator<T> iterator = iterator();
		while(iterator.hasNext()) {
			add(iterator.next());
		}
	}
	
	public BigDecimal sum() {
	    BigDecimal sum = new BigDecimal(0);
	    for (T value : list) {
	    	if(value instanceof Number) {
	    		sum.add(BigDecimal.valueOf(((Number)value).doubleValue()));
	    	}
	    }
	    return sum;
	}
	
	public boolean add(T item) {		
		return list.add(item);
	}
	
	public T get(int index) {
		return list.get(index);
	}
	
	public T get(T item) {
		int index = indexOf(item);
		if(index == -1) {
			return null;
		}else{
			return get(index);
		}		
	}
	
	public int size() {
		return list.size();
	}
	
	public int count() {
		return size();
	}
	
	public void clear() {
		list.clear();
	}
	
	public List<T> toList() {
		return list;
	}
	
	public Enumerable<T> subList(int fromIndex, int toIndex) {
		return new Enumerable<T>(subList(fromIndex, toIndex).toList());
	}
	
	public T first() {
		return isEmpty() ? null : get(0);
	}
	
	public T last() {
		return isEmpty() ? null : get(size()-1);
	}
	
	public T firstOrDefault() {
		return isEmpty() ? defaultValue : get(0);
	}
	
	public T lastOrDefault() {
		return isEmpty() ? defaultValue : get(size()-1);
	}
	
	public T firstOrNull() {
		return isEmpty() ? null : get(0);
	}	
	
	public T lastOrNull() {
		return isEmpty() ? null : get(size()-1);
	}	
	
	public T getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(T value) {
		defaultValue = value;
	}
		
	public void add(int index, T element) {
		list.add(index, element);
	}
	
	public boolean add(Collection<? extends T> c) {
		return list.addAll(c);
	}	
	
	public boolean addAll(int index, Collection<? extends T> c) {
		return list.addAll(index, c);
	}	
	
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}	
	
	public boolean equals(Object o) {
		return list.equals(o);
	}	
	
	public void forEach(Consumer<? super T> action) {
		list.forEach(action);
	}	
	
	public int hashCode() {
		return list.hashCode();
	}	
	
	public boolean isEmpty() {
		return list.isEmpty();
	}	
	
	public T defaultIfEmpty() {
		if(isEmpty()) {
			return defaultValue;
		}else{
			return null;
		}
	}
	
	public String nullIfEmpty() {
		if(isEmpty()) {
			return null;
		}else{
			return "";
		}
	}
	
	public Iterator<T> iterator() {
		return list.iterator();
	}	
	
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}	
	
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}	
	
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}	
	
	public Stream<T> parallelStream() {
		return list.parallelStream();
	}
	
	public T remove(int index) {
		return list.remove(index);
	}
	
	public boolean removeObject(Object object) {
		return list.remove(object);
	}
	
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	
	public boolean removeIf(Predicate<? super T> filter) {
		return list.removeIf(filter);
	}
	
	public void replaceAll(UnaryOperator<T> operator) {
		list.replaceAll(operator);
	}
	
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	
	public T set(int index, T element) {
		return list.set(index, element);
	}
	
	public void sort(Comparator<? super T> c) {
		list.sort(c);
	}
	
	public Enumerable<T> sortAndReturn(Comparator<? super T> c) {
		sort(c);
		return this;
	}
	
	public Spliterator<T> spliterator() {
		return list.spliterator();
	}
	
	public Stream<T> stream() {
		return list.stream();
	}
	
	public Object[] toArray() {
		return list.toArray();
	}
	
	public HashSet<T> toHashSet(){
		return new HashSet<T>(list);
	}
	
	public String toString() {
		return list.toString();
	}
	
	@Override
	public boolean contains(Object object) {
		return list.contains(object);
	}
	
	@Override
	public boolean remove(Object item) {
		return list.remove(item);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return list.addAll(c);
	}

	@Override
	public int indexOf(Object object) {
		return list.indexOf(object);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
}