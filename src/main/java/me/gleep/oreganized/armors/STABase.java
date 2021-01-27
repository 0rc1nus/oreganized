package me.gleep.oreganized.armors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class STABase extends ArmorItem {
    private final boolean immuneToFire;
    public STABase(IArmorMaterial materialIn, EquipmentSlotType slot, boolean immuneToFire) {
        super(materialIn, slot, new Properties().group(ItemGroup.COMBAT).maxStackSize(1).setNoRepair());
        this.immuneToFire = immuneToFire;
    }

    public STABase(IArmorMaterial materialIn, EquipmentSlotType slot) {
        super(materialIn, slot, new Properties().group(ItemGroup.COMBAT).maxStackSize(1).setNoRepair());
        this.immuneToFire = false;
    }

    @Override
    public boolean isImmuneToFire() {
        return this.immuneToFire;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        if (stack.getItemEnchantability() == ArmorMaterial.GOLD.getEnchantability()) {
            return true;
        } else return false;
    }
}
