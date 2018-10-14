package mirrg.minecraft.entitycount;

import net.minecraft.entity.Entity;

public class ClassEntry implements Comparable<ClassEntry>
{

	public final Class<? extends Entity> clazz;
	public final String classHierarchicalName;

	public ClassEntry(Class<? extends Entity> clazz)
	{
		this.clazz = clazz;
		this.classHierarchicalName = getClassHierarchicalName(clazz);
	}

	private static String getClassHierarchicalName(Class<?> clazz)
	{
		String string = "";

		while (true) {
			string = clazz.getSimpleName() + string;
			if (clazz == Object.class) break;
			clazz = clazz.getSuperclass();
		}

		return string;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ClassEntry other = (ClassEntry) obj;
		if (clazz == null) {
			if (other.clazz != null) return false;
		} else if (!clazz.equals(other.clazz)) return false;
		return true;
	}

	@Override
	public int compareTo(ClassEntry o)
	{
		return classHierarchicalName.compareTo(o.classHierarchicalName);
	}

}
