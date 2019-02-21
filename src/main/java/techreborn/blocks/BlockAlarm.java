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

package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;
import techreborn.tiles.TileAlarm;
import javax.annotation.Nullable;
import java.util.List;

public class BlockAlarm extends BaseTileBlock {
	public static DirectionProperty FACING;
	public static BooleanProperty ACTIVE;
	protected final VoxelShape[] shape;

	public BlockAlarm() {
		super(Block.Properties.create(Material.ROCK));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(ACTIVE, false));
		this.shape = GenCuboidShapes(0.19, 0.81);
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/lighting"));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}
	
	private VoxelShape[] GenCuboidShapes(double depth, double width) {
		VoxelShape[] shapes = {
				Block.makeCuboidShape(width, 1.0 - depth, width, 1.0 - width, 1.0D, 1.0 - width),
				Block.makeCuboidShape(width, 0.0D, width, 1.0 - width, depth, 1.0 - width),
				Block.makeCuboidShape(width, width, 1.0 - depth, 1.0 - width, 1.0 - width, 1.0D),
				Block.makeCuboidShape(width, width, 0.0D, 1.0 - width, 1.0 - width, depth),
				Block.makeCuboidShape(1.0 - depth, width, width, 1.0D, 1.0 - width, 1.0 - width),
				Block.makeCuboidShape(0.0D, width, width, depth, 1.0 - width, 1.0 - width),	
		};
		return shapes;
	}
	
	public static boolean isActive(IBlockState state) {
		return state.get(ACTIVE);
	}

	public static EnumFacing getFacing(IBlockState state) {
		return (EnumFacing) state.get(FACING);
	}

	public static void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	public static void setActive(boolean active, World world, BlockPos pos) {
		EnumFacing facing = world.getBlockState(pos).get(FACING);
		IBlockState state = world.getBlockState(pos).with(ACTIVE, active).with(FACING, facing);
		world.setBlockState(pos, state, 3);
	}
	
	// Block
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		FACING = DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);
		ACTIVE = BooleanProperty.create("active");
		builder.add(FACING, ACTIVE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We extended BaseTileBlock. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, side)) {
				return true;
			}
		}

		if (!worldIn.isRemote && playerIn.isSneaking()) {
			((TileAlarm) tileEntity).rightClick();
			return true;

		}

		return super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileAlarm();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TextComponentTranslation("techreborn.tooltip.alarm").applyTextStyle(TextFormatting.GRAY));
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return shape[getFacing(state).getIndex()];
	}
}
