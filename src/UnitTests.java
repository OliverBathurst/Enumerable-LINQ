import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.jupiter.api.Test;

class UnitTests {

	@Test
	public void skip() {
		Enumerable<Integer> enumerable = enumerableMock();
		assertEquals(enumerable.size(), 20);
		
		enumerable = enumerable.skip(10);
		assertEquals(enumerable.size(),10);
		
		assertEquals(enumerable.first(), (Integer)11);
		assertEquals(enumerable.last(), (Integer)20);
	}
	
	@Test
	public void skipWhile() {
		Enumerable<Integer> skipped =
				enumerableMock().skipWhile(x -> x < 10);
		
		assertEquals(skipped.size(), 11);
		assertEquals(skipped.first(), (Integer)10);
		assertEquals(skipped.last(), (Integer)20);
	}
	
	@Test
	public void take() {
		Enumerable<Integer> result = enumerableMock().take(4);
		assertEquals(result.size(), 4);
		assertEquals(result.first(), (Integer)1);
		assertEquals(result.last(), (Integer)4);
	}
	
	@Test
	public void orReturn() {
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.select(x -> x > 0);
		assertEquals(result.size(), 20);
		assertEquals(result.orReturn(7), (Integer)1);

		Integer result2 = enumerable.select(x -> x > 100)
				.orReturn(7);
		assertEquals(result2, (Integer)7);
	}
	
	@Test	
	public void select() {
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.select(x -> x > 0);
		assertEquals(result.size(), 20);
		
		result = enumerable.select(x -> x > 10);
		assertEquals(result.size(), 10);
	}
	
	@Test
	public void where() {
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.where(x -> x > 0);
		assertEquals(result.size(), 20);
		
		result = enumerable.where(x -> x > 10);
		assertEquals(result.size(), 10);
	}
	
	@Test
	public void takeWhile() {
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.takeWhile(x -> x < 21);
		
		assertEquals(result.size(), 20);
		
		Enumerable<Integer> result2 = enumerable
				.takeWhile(x -> x < 15);
		
		assertEquals(result2.size(), 14);
		assertEquals(result2.last(), (Integer)14);
		assertEquals(result2.first(), (Integer)1);
	}
	
	@Test
	public void ofType(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result = enumerable.ofType(Integer.class);
		assertEquals(result.size(), 20);
		
		result = enumerable.ofType(String.class);
		assertEquals(result.size(), 0);
		assertEquals(result.isEmpty(), true);
	}
	
	@Test
	public void filter(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.filter(x -> x > 0);
		assertEquals(result.size(), 20);
		
		result = enumerable.filter(x -> x > 10);
		assertEquals(result.size(), 10);
	}
	
	@Test
	public void and(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> result =
				enumerable.select(x -> x > 0);
		assertEquals(result.size(), 20);
		
		result = enumerable.select(x -> x > 10)
				.and(x -> x > 15);
		assertEquals(result.size(), 5);
	}
	
	@Test
	public void thenBy(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> sorted = enumerable.sortAndReturn(new Comparator<Integer>() {
	        public int compare(final Integer o1, final Integer o2) {	           
	            return o1 - o2;
	        }
	    });
		assertEquals(sorted.last(), (Integer)20);
		
		sorted = enumerable.sortAndReturn(new Comparator<Integer>() {
	        public int compare(final Integer o1, final Integer o2) {	           
	            return o1 - o2;
	        }
	    }).thenBy(new Comparator<Integer>() {
	        public int compare(final Integer o1, final Integer o2) {	           
	            return o2 - o1;
	        }
	    });
		assertEquals(sorted.last(), (Integer)1);		
	}
	
	@Test
	public void intersect(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> enumerable2 = enumerableMock2();
		Enumerable<Integer> result = enumerable.intersect(enumerable2);
		
		assertEquals(result, enumerable2);
		assertEquals(result.size(), 6);
		assertEquals(result.first(), (Integer)15);
		assertEquals(result.last(), (Integer)20);
	}
	
	@Test
	public void areDisjoint(){
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> enumerable2 = enumerableMock2();
		
		assertEquals(enumerable.areDisjoint(enumerable), false);
		assertEquals(enumerable.areDisjoint(enumerableMock2()), false);
		assertEquals(enumerable.areDisjoint(enumerableMock3()), true);
		assertEquals(enumerable2.areDisjoint(enumerableMock3()), true);
	}
	
	@Test
	public void sequenceEval() {
		Enumerable<Integer> enumerable = enumerableMock();
		Enumerable<Integer> enumerable2 = enumerableMock();
		assertEquals(enumerable.sequenceEval(enumerable2), true);
		
		enumerable2.add((Integer)99);
		assertEquals(enumerable.sequenceEval(enumerable2), false);
		
		enumerable2 = enumerableMock();
		assertEquals(enumerable.sequenceEval(enumerable2), true);
		
		enumerable2.set(10, (Integer)20);
		assertEquals(enumerable.sequenceEval(enumerable2), false);	
	}
	
	@Test
	public void elementAtOrDefault(){
		Enumerable<Integer> enumerable = enumerableMock();
		assertEquals(enumerable.elementAtOrDefault(enumerable.size()), null);
		assertEquals(enumerable.elementAtOrDefault(0), (Integer)1);
		
		enumerable.setDefaultValue(999);
		assertEquals(enumerable.elementAtOrDefault(enumerable.size()), (Integer)999);
	}
	
	@Test
	public void distinct() {
		Enumerable<Integer> enumerable = enumerableMock();
		
		assertEquals(enumerable.distinct().size(), 20);
		
		enumerable.add((Integer)10);
		enumerable.add((Integer)11);
		enumerable.add((Integer)12);
		
		assertEquals(enumerable.distinct().size(), 20);
	}
	
	@Test
	public void single() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.single(x -> x > 20), null);
		
		enumerable.add(999);
		assertEquals(enumerable.single(x -> x > 20), (Integer)999);
		
		enumerable.add(999);
		assertEquals(enumerable.single(x -> x > 20), null);
	}
	
	@Test
	public void get() {
		assertEquals(enumerableMock().get(5), (Integer)6);
	}
	
	@Test
	public void first() {
		assertEquals(enumerableMock().first(), (Integer)1);
	}
	
	@Test
	public void last() {
		assertEquals(enumerableMock().last(), (Integer)20);
	}
	
	@Test
	public void firstOrDefault() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.firstOrDefault(), null);
		
		enumerable.add(999);
		assertEquals(enumerable.firstOrDefault(), (Integer)999);
		
		enumerable.clear();
		assertEquals(enumerable.firstOrDefault(), null);
		
		enumerable.setDefaultValue(999);
		assertEquals(enumerable.firstOrDefault(), (Integer)999);
	}
	
	@Test
	public void lastOrDefault() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.lastOrDefault(), null);
		
		enumerable.add(999);
		assertEquals(enumerable.lastOrDefault(), (Integer)999);
		
		enumerable.clear();
		assertEquals(enumerable.lastOrDefault(), null);
		
		enumerable.setDefaultValue(999);
		assertEquals(enumerable.lastOrDefault(), (Integer)999);
	}
	
	@Test
	public void firstOrNull() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.firstOrNull(), null);
		
		enumerable.add(999);
		assertEquals(enumerable.firstOrNull(), (Integer)999);
	}
	
	@Test
	public void lastOrNull() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.lastOrNull(), null);
		
		enumerable.add(999);
		assertEquals(enumerable.lastOrNull(), (Integer)999);
	}
	
	@Test
	public void getDefaultValue() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.getDefaultValue(), null);
		
		enumerable.setDefaultValue(999);
		assertEquals(enumerable.getDefaultValue(), (Integer)999);		
	}
	
	@Test	
	public void defaultIfEmpty() {
		Enumerable<Integer> enumerable = new Enumerable<>();
		assertEquals(enumerable.defaultIfEmpty(), null);
		
		enumerable.setDefaultValue(999);
		assertEquals(enumerable.defaultIfEmpty(), (Integer)999);
	}
	
	@Test
	public void nullIfEmpty() {;
		assertEquals(new Enumerable<Integer>().nullIfEmpty(), null);
	}
	
	private Enumerable<Integer> enumerableMock() {
		return new Enumerable<Integer>(
				Arrays.asList(1,2,3,4,5,6,7,8,9,10,
						11,12,13,14,15,16,17,18,19,20));
	}
	
	private Enumerable<Integer> enumerableMock2() {
		return new Enumerable<Integer>(
				Arrays.asList(15,16,17,18,19,20));
	}
	
	private Enumerable<Integer> enumerableMock3() {
		return new Enumerable<Integer>(
				Arrays.asList(21,22,23,24,25,26));
	}
}
