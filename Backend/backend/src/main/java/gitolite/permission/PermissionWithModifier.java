package gitolite.permission;

import com.google.common.collect.Sets;
import lombok.Value;

import java.util.Arrays;
import java.util.SortedSet;

/**
 * @author Jan-Willem Gmelig Meyling
 */
@Value
public class PermissionWithModifier implements Permission {

	private final BasePermission basePermission;

	private final SortedSet<PermissionModifier> modifiers;

	public PermissionWithModifier(BasePermission basePermission, PermissionModifier... modifiers) {
		this(basePermission, Sets.newTreeSet(Arrays.asList(modifiers)));
	}

	public PermissionWithModifier(BasePermission basePermission, SortedSet<PermissionModifier> modifiers) {
		this.basePermission = basePermission;
		this.modifiers = modifiers;
	}

	@Override
	public String valueOf() {
		StringBuilder builder = new StringBuilder(3 + modifiers.size());
		builder.append(basePermission.valueOf());
		for(PermissionModifier permissionModifier : modifiers) {
			builder.append(permissionModifier.toString());
		}
		return builder.toString();
	}
}
