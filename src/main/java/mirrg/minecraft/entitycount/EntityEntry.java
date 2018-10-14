package mirrg.minecraft.entitycount;

import net.minecraft.entity.Entity;

public class EntityEntry
{

	public final ClassEntry classEntry;

	public final String uuid;
	public final String name;
	public final String string;
	public final double x;
	public final double y;
	public final double z;

	public EntityEntry(Class<? extends Entity> clazz, String uuid, String name, String string, double x, double y, double z)
	{
		this.classEntry = new ClassEntry(clazz);
		this.uuid = uuid;
		this.name = name;
		this.string = string;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString()
	{
		return "" + classEntry.clazz.getName() + "\t" + uuid + "\t" + name + "\t" + x + "\t" + y + "\t" + z + "\t" + string;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		EntityEntry other = (EntityEntry) obj;
		if (uuid == null) {
			if (other.uuid != null) return false;
		} else if (!uuid.equals(other.uuid)) return false;
		return true;
	}

}
