package com.company.onlinestore;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;

public class ThreeDeeAddOnServiceTest {

	private ThreeDeeAddOnServiceImpl service = new ThreeDeeAddOnServiceImpl();
	private Basket basket;
	
	@Before
	public void setup() throws TechnicalFailureException {

		//setup the basket
		basket = new Basket();
		basket.add("SPORTS");
		basket.add("KIDS");
		basket.add("VARIETY");
		basket.add("NEWS");
		basket.add("MOVIES_1");
		basket.add("MOVIES_2");
		
		AvailabilityChecker checker = Mockito.mock(AvailabilityChecker.class);
		service.setChecker(checker);

		Mockito.when(checker.isPostCodeIn3DTVServiceArea("3000")).thenReturn("SERVICE_AVAILABLE");
		Mockito.when(checker.isPostCodeIn3DTVServiceArea("4000")).thenReturn("SERVICE_UNAVAILABLE");
		Mockito.when(checker.isPostCodeIn3DTVServiceArea("5000")).thenReturn("SERVICE_PLANNED");
		Mockito.when(checker.isPostCodeIn3DTVServiceArea("000")).thenReturn("POSTCODE_INVALID");
		Mockito.when(checker.isPostCodeIn3DTVServiceArea("XXXX")).thenThrow(new TechnicalFailureException());
		
	}
		
	@Test
	public void testServiceAvailable() {
		//Service is Available
		service.checkFor3DAddOnProducts(basket, "3000");
		List<String> actual = basket.getContents();
		
		//output - return relevant 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("SPORTS_3D_ADD_ON");
		expected.add("KIDS");
		expected.add("VARIETY");
		expected.add("NEWS_3D_ADD_ON");
		expected.add("MOVIES_3D_ADD_ON");
		expected.add("MOVIES_3D_ADD_ON");
		
		assertArrayEquals(expected.toArray(), actual.toArray());
	}
	
	@Test
	public void testPostCodeIsNull() {
		//Post Code is Null and no products with 3D compatible in the basket
		basket = new Basket();
		basket.add("KIDS");
		basket.add("VARIETY");
		
		service.checkFor3DAddOnProducts(basket, null);
		List<String> actual = basket.getContents();
				
		//output - return no 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("KIDS");
		expected.add("VARIETY");
				
		assertArrayEquals(expected.toArray(), actual.toArray());
	}
	
	@Test
	public void testServiceUnavailable() {
		//Service Unavailable		
		service.checkFor3DAddOnProducts(basket, "4000");
		List<String> actual = basket.getContents();
		
		//output - return no 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("SPORTS");
		expected.add("KIDS");
		expected.add("VARIETY");
		expected.add("NEWS");
		expected.add("MOVIES_1");
		expected.add("MOVIES_2");
		
		assertArrayEquals(expected.toArray(), actual.toArray());
	}
	
	@Test
	public void testServicePlanned() {
		//Service Planned
		service.checkFor3DAddOnProducts(basket, "5000");		
		List<String> actual = basket.getContents();
		
		//output - return no 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("SPORTS");
		expected.add("KIDS");
		expected.add("VARIETY");
		expected.add("NEWS");
		expected.add("MOVIES_1");
		expected.add("MOVIES_2");
		
		assertArrayEquals(expected.toArray(), actual.toArray());
	}

	@Test
	public void testInvalidPostCode() {
		//Invalid Post Code
		service.checkFor3DAddOnProducts(basket, "000");
		List<String> actual = basket.getContents();
		
		//output - return no 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("SPORTS");
		expected.add("KIDS");
		expected.add("VARIETY");
		expected.add("NEWS");
		expected.add("MOVIES_1");
		expected.add("MOVIES_2");
		
		assertArrayEquals(expected.toArray(), actual.toArray());
	}

	@Test
	public void testTechnicalFailureOccurs() {
		//Exception is thrown when checking 3D availability
		service.checkFor3DAddOnProducts(basket, "XXXX");
		List<String> actual = basket.getContents();
		
		//output - return no 3D add-ons
		List<String> expected = new ArrayList<String>();
		expected.add("SPORTS");
		expected.add("KIDS");
		expected.add("VARIETY");
		expected.add("NEWS");
		expected.add("MOVIES_1");
		expected.add("MOVIES_2");
		
		assertArrayEquals(expected.toArray(), actual.toArray());
	}
}
