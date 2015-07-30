package net.djp3.qualoscopy.api.procedure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;

public class QAPIEvent_Procedures_Test_Utilities {

	public static void checkProcedure(JSONObject procedure) {
		assertEquals(procedure.size(),30);
		assertTrue(procedure.get("procedure_id") != null);
		assertTrue(procedure.get("ac_id") != null);
		assertTrue(procedure.get("faculty_id") != null);
		assertTrue(procedure.get("completed") != null);
		assertTrue(procedure.get("location") != null);
		assertTrue(procedure.get("fellow") != null);
		assertTrue(procedure.get("pre_drug") != null);
		assertTrue(procedure.get("prep_liters") != null);
		assertTrue(procedure.get("split_prep") != null);
		assertTrue(procedure.get("bisacodyl") != null);
		assertTrue(procedure.get("last_colon") != null);
		assertTrue(procedure.get("primary_indication") != null);
		assertTrue(procedure.get("other_indication") != null);
		assertTrue(procedure.get("scope") != null);
		assertTrue(procedure.get("endocuff") != null);
		assertTrue(procedure.get("cap_assisted") != null);
		assertTrue(procedure.get("underwater") != null);
		assertTrue(procedure.get("sedation_level") != null);
		assertTrue(procedure.get("versed") != null);
		assertTrue(procedure.get("fentanyl") != null);
		assertTrue(procedure.get("demerol") != null);
		assertTrue(procedure.get("benadryl") != null);
		assertTrue(procedure.get("extent") != null);
		assertTrue(procedure.get("prep_quality_left") != null);
		assertTrue(procedure.get("prep_quality_mid") != null);
		assertTrue(procedure.get("prep_quality_right") != null);
		assertTrue(procedure.get("time_insertion") != null);
		assertTrue(procedure.get("time_begin_withdrawal") != null);
		assertTrue(procedure.get("time_scope_withdrawn") != null);
		assertTrue(procedure.get("date_time_of_service") != null);
	}

}
