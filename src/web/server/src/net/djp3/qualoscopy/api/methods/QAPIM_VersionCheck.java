package net.djp3.qualoscopy.api.methods;

import net.djp3.qualoscopy.api.handlers.H_VersionCheck;
import edu.uci.ics.luci.utility.webserver.disruptor.eventhandlers.WebEventHandler;
import edu.uci.ics.luci.utility.webserver.disruptor.events.WebEventInterface;
import edu.uci.ics.luci.utility.webserver.disruptor.events.server.ServerCall;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;

public class QAPIM_VersionCheck extends ServerCall implements WebEventInterface{
	
	public QAPIM_VersionCheck(Request request, Output output) {
		super(request, output);
	}

	@Override
	public Class<? extends WebEventHandler> getMatchingHandler(){
		return H_VersionCheck.class;
	}

}
