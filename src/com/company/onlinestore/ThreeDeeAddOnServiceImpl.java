package com.company.onlinestore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.acme.serviceavailability.AvailabilityChecker;
import com.acme.serviceavailability.TechnicalFailureException;

import static com.company.onlinestore.Availability.*;
import static com.company.onlinestore.ProductCode.*;
import static com.company.onlinestore.ProductCodeAddOn.*;

public class ThreeDeeAddOnServiceImpl implements ThreeDeeAddOnService {

	private AvailabilityChecker checker;
	private static Logger logger = Logger.getLogger("com.company.onlinestore.ThreeDeeAddOnServiceImpl");
	
	@Override
	public void checkFor3DAddOnProducts(Basket basket, String postCode) {
		List<String> list = basket.getContents();
		List<String> newList = new ArrayList<String>();
		
		String status = check3DTVAvailability(postCode);
		if (SERVICE_AVAILABLE.toString().equals(status)) {
			logger.log(Level.INFO, "Status: SERVICE_AVAILABLE for Post Code " + postCode);
			for(String item: list) {
				if (SPORTS.toString().equals(item)) {
					newList.add(SPORTS_3D_ADD_ON.toString());
				} else if (NEWS.toString().equals(item)) {
					newList.add(NEWS_3D_ADD_ON.toString());
				} else if (MOVIES_1.toString().equals(item) ||
						(MOVIES_2.toString().equals(item))) {
					newList.add(MOVIES_3D_ADD_ON.toString());
				} else {
					newList.add(item);
				}
			}
		} else {
		
			newList.addAll(list);
			if (SERVICE_UNAVAILABLE.toString().equals(status)) {
				logger.log(Level.INFO, "Status: SERVICE_UNAVAILABLE for Post Code " + postCode);
				
			} else if (SERVICE_PLANNED.toString().equals(status)) {
				logger.log(Level.INFO, "Status: SERVICE_PLANNED for Post Code " + postCode);
				
			} else if (POSTCODE_INVALID.toString().equals(status)) {
				logger.log(Level.INFO, "Status: POSTCODE_INVALID - " + postCode);				
				//notify the client
				basket.sendClientNotification();
			} else {
				logger.log(Level.INFO, "Status: " + status);
			}
		}
		
		basket.setContents(newList);
		logger.log(Level.INFO, "Basket contents: " + newList);
	}
	
	private String check3DTVAvailability(String postCode) {
		String status = null;
		try {
			status = checker.isPostCodeIn3DTVServiceArea(postCode);
		} catch (TechnicalFailureException e) {
			logger.log(Level.SEVERE, "Status: Technical Failure Exception occured");
		}
		return status;
	}
	
	public AvailabilityChecker getChecker() {
		return checker;
	}
	
	public void setChecker(AvailabilityChecker checker) {
		this.checker = checker;
	}
}
