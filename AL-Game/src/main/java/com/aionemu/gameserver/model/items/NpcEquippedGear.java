/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.items;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.aionemu.gameserver.dataholders.loadingutils.adapters.NpcEquipmentList;
import com.aionemu.gameserver.dataholders.loadingutils.adapters.NpcEquippedGearAdapter;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author Luno
 * 
 */
@XmlJavaTypeAdapter(NpcEquippedGearAdapter.class)
public class NpcEquippedGear
{
	private Map<ItemSlot, ItemTemplate>	items;
	private short						mask;
	
	private NpcEquipmentList	v;
	public NpcEquippedGear(NpcEquipmentList v)
	{
		this.v = v;
	}

	/**
	 * @return short
	 */
	public short getItemsMask()
	{
		if(items == null) 
			init();
		
		return mask;
	}
	
	public Collection<Entry<ItemSlot, ItemTemplate>> getItems()
	{
		if(items == null) 
			init();
		
		return items.entrySet();
	}

	/**
	 *  Here NPC equipment mask is initialized.
	 *  All NPC slot masks should be lower than 65536
	 */
	public void init()
	{
		synchronized(this)
		{
			if(items == null)
			{
				items = new TreeMap<ItemSlot, ItemTemplate>();
				for(ItemTemplate item : v.items)
				{
					List<ItemSlot> itemSlots = ItemSlot.getSlotsFor(item.getItemSlot());
					for(ItemSlot itemSlot : itemSlots)
					{
						if(items.get(itemSlot) == null)
						{
							items.put(itemSlot, item);
							mask |= itemSlot.getSlotIdMask();
							break;
						}
					}	
				}
			}
			v = null;
		}
	}
	
	/**
	 * 
	 * @param itemSlot
	 * @return
	 */
	public ItemTemplate getItem(ItemSlot itemSlot)
	{
		return items != null ? items.get(itemSlot) : null;
	}
}