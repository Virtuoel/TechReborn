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

package techreborn.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.api.tile.IUpgrade;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.tile.TileMachineBase;
import techreborn.TechReborn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemUpgrade extends Item implements IUpgrade {

	public final String name;
	public final IUpgrade behavior;

	public ItemUpgrade(String name, IUpgrade process) {
		super(new Item.Properties().group(TechReborn.ITEMGROUP).maxStackSize(16));
		this.name = name;
		this.behavior = process;
	}

	@Override
	public void process(
		@Nonnull
			TileMachineBase tile,
		@Nullable
			IUpgradeHandler handler,
		@Nonnull
			ItemStack stack) {
		behavior.process(tile, handler, stack);
	}
}