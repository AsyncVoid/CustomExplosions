package me.Async.CE.enums;

public enum TNTType
{
	SMOKE,
	NAIL,
	NAPALM,
	TOXIC,
	BUNKER,
	CLUSTER,
	NUKE,
	SUICIDE,
	WATER,
	ROCKET,
	FOAMER,
	EXTREME;
	
	public static TNTType getFromID(int id)
	{
		return TNTType.values()[id - 1];
	}
	
	public int getID()
	{
		return this.ordinal() + 1;
	}
}
