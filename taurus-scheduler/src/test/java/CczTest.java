import org.junit.Test;

public class CczTest {

	@Test
	public void testParsreNum(){
		int agentid = 12;
		String aIdStr = null;
		try {
			agentid = Integer.parseInt(aIdStr);
		} catch (NumberFormatException e) {
			stdout(aIdStr + " is not the valid number!");
		}
		stdout(agentid);
	}

	public void stdout(Object msg) {
		System.out.println(msg);
	}
}
