/*
 * Copyright (C) 2014 Benjamin Arnold
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.common.EntityTypes;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Benjamin Arnold
 */
public class EntityTypeGeneralizationLevelTest {
    
    @Test
    public void validProperty() throws Exception {
	final int expectedCompanyLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedOccupationLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedDrugAlcoholLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedEmotionLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedFamilyLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedLocationLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedMedicalLevel=2;
	final int expectedPersonalAttacksLevel=EntityTypes.DEFAULT_LEVEL;
	final int expectedSterotypingLevel=EntityTypes.DEFAULT_LEVEL;
		
	assertEquals(expectedCompanyLevel,EntityTypes.COMPANY.getGeneralizationLevel());
	assertEquals(expectedOccupationLevel,EntityTypes.OCCUPATION.getGeneralizationLevel());
	assertEquals(expectedDrugAlcoholLevel,EntityTypes.DRUG_ALCOHOL.getGeneralizationLevel());
	assertEquals(expectedEmotionLevel,EntityTypes.EMOTION.getGeneralizationLevel());
	assertEquals(expectedFamilyLevel,EntityTypes.FAMILY.getGeneralizationLevel());
	assertEquals(expectedLocationLevel,EntityTypes.LOCATION.getGeneralizationLevel());
	assertEquals(expectedMedicalLevel,EntityTypes.MEDICAL.getGeneralizationLevel());
	assertEquals(expectedPersonalAttacksLevel,EntityTypes.PERSONAL_ATTACKS.getGeneralizationLevel());
	assertEquals(expectedSterotypingLevel,EntityTypes.STEREOTYPING.getGeneralizationLevel());
    }
}
