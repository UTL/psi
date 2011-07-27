package webApplication.grafica;

import java.util.EventObject;

public interface MyEventClassListener {
	public abstract void handleMyEventClassEvent(EventObject e);

	void handleMyEventClassEvent(MyEventClass e);
}