/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.api.recipe.recipes;

import com.google.gson.JsonObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import reborncore.common.crafting.Recipe;
import reborncore.common.crafting.RecipeType;
import techreborn.tiles.machine.multiblock.TileIndustrialBlastFurnace;

public class BlastFurnaceRecipe extends Recipe {

	int heat;

	public BlastFurnaceRecipe(RecipeType<?> type, ResourceLocation name) {
		super(type, name);
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		super.deserialize(jsonObject);
		heat = JsonUtils.getInt(jsonObject, "heat");
	}

	@Override
	public void serialize(JsonObject jsonObject) {
		super.serialize(jsonObject);
		jsonObject.addProperty("heat", heat);
	}

	@Override
	public boolean canCraft(final TileEntity tile) {
		if (tile instanceof TileIndustrialBlastFurnace) {
			final TileIndustrialBlastFurnace blastFurnace = (TileIndustrialBlastFurnace) tile;
			return blastFurnace.getHeat() >= heat;
		}
		return false;
	}

	@Override
	public boolean onCraft(final TileEntity tile) {
		return true;
	}
}