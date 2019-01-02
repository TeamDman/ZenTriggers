package zentriggers.zentriggers.actions;

import zentriggers.zentriggers.TriggerType;

public interface IAction<T> {
	public TriggerType getType();
	public void accept(T arg);
}
