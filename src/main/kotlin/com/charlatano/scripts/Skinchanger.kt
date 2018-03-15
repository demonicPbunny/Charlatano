package com.charlatano.scripts

import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.engineDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.entity.Player
import com.charlatano.utils.extensions.uint
import com.charlatano.game.offsets.EngineOffsets.dwClientState
import com.charlatano.game.offsets.ClientOffsets.dwLocalPlayer
import com.charlatano.game.netvars.NetVarOffsets.hMyWeapons
import com.charlatano.game.netvars.NetVarOffsets.iItemDefinitionIndex
import com.charlatano.game.netvars.NetVarOffsets.nFallbackPaintKit
import com.charlatano.game.netvars.NetVarOffsets.iEntityQuality
import com.charlatano.game.netvars.NetVarOffsets.nFallbackStatTrak
import com.charlatano.game.netvars.NetVarOffsets.flFallbackWear
import com.charlatano.game.offsets.ClientOffsets.dwEntityList
import com.charlatano.game.netvars.NetVarOffsets.iItemIDHigh
import com.charlatano.game.netvars.NetVarOffsets.nFallbackSeed
import com.charlatano.game.offsets.EngineOffsets
import com.charlatano.scripts.SkinChangerPlugin.weaponAddress
import com.charlatano.scripts.SkinChangerPlugin.weapon
import org.jire.arrowhead.keyPressed
import com.charlatano.game.Weapons
import com.charlatano.utils.every
object SkinChangerPlugin {
	val engine = engineDLL.uint(dwClientState)
	

//	val type by lazy(LazyThreadSafetyMode.NONE) { Weapons.byID(id) }

var weaponAddress = 0
lateinit var weapon: Weapons

private fun skins() {
//// Helpers for quality
	/*
	{0, "Default"},
	{1, "Genuine"}, For Stattrak
	{2, "Vintage"},
	{3, "Unusual"},
	{5, "Community"},
	{6, "Developer"},
	{7, "Self-Made"},
	{8, "Customized"},
	{9, "Strange"},
	{10, "Completed"},
	{12, "Tournament"} For Souvenir
	*/
	    Weapons.AK47(474, 6)
		Weapons.AUG(455, 0)
		Weapons.AWP(344, 12)
		Weapons.CZ75A(687, 1, 231)
		Weapons.DESERT_EAGLE(61, 1, 132)
		Weapons.FAMAS(154, 0 )
		Weapons.FIVE_SEVEN(427, 0)
		Weapons.G3SG1(511, 0)
		Weapons.GALIL(379, 12)
		Weapons.GLOCK(353, 1, 231)
		Weapons.M249(496, 0)
		Weapons.M4A1_SILENCER(644, 1, 212)
		Weapons.M4A4(155, 1, 1321)
		Weapons.MAC10(482, 9)
		Weapons.MAG7(431, 0)
		Weapons.MP7(482, 0)
		Weapons.MP9(482, 0)
		Weapons.NEGEV(317, 0)
		Weapons.NOVA(286, 0)
		Weapons.P2000(482, 0)
		Weapons.P250(482, 0)
		Weapons.P90(482, 0)
		Weapons.PP_BIZON(542, 0)
		Weapons.R8_REVOLVER(595, 0)
		Weapons.SAWED_OFF(256, 0)
		Weapons.SCAR20(391, 0)
		Weapons.SSG08(624, 0)
		Weapons.SG556(287, 0)
		Weapons.TEC9(287, 0)
		Weapons.UMP45(556, 1, 231)
		Weapons.USP_SILENCER(183, 1, 21)
		Weapons.XM1014(393, 0)
	}


fun skinwepindex() = every(1) {
	for (i in 1..3) try {
		
		val me: Player = clientDLL.uint(dwLocalPlayer)
        var currentWeaponIndex = csgoEXE.uint(me + hMyWeapons + ((i - 1) * 0x4))
       currentWeaponIndex = currentWeaponIndex and 0xFFF
	   weaponAddress = clientDLL.int(dwEntityList + (currentWeaponIndex - 1) * 0x10)
        if (weaponAddress <= 0) continue
        val weaponID = csgoEXE.int(weaponAddress + iItemDefinitionIndex)
       

       csgoEXE[weaponAddress + iItemIDHigh] = -1
		
       weapon = Weapons[weaponID]
       skins()

    } catch (t: Throwable) {

    }
     val enginePointer = engineDLL.uint(dwClientState)
if (keyPressed(67)) csgoEXE[enginePointer + 0x174] = -1
}

}
val DEFAULT_STATTRAK = -1
val DEFAULT_SKIN_SEED = 1

val DEFAULT_WEAR = 0.0001F // lower = less wear, higher = more wear
val DEFAULT_QUALITY = 0
private fun skin(skinID: Int, skinSeed: Int, statTrak: Int, wear: Float, quality: Int){

        csgoEXE[SkinChangerPlugin.weaponAddress + nFallbackPaintKit] = skinID
		csgoEXE[SkinChangerPlugin.weaponAddress + nFallbackSeed] = skinSeed
		csgoEXE[SkinChangerPlugin.weaponAddress + nFallbackStatTrak] = statTrak
		csgoEXE[SkinChangerPlugin.weaponAddress + iEntityQuality] = quality
		csgoEXE[SkinChangerPlugin.weaponAddress + flFallbackWear] = wear
}
private operator fun Weapons.invoke(skinID: Int, skinSeed: Int = DEFAULT_SKIN_SEED,
	                                    statTrak: Int = DEFAULT_STATTRAK, wear: Float = DEFAULT_WEAR,
	                                    quality: Int = DEFAULT_QUALITY) {

	if (this ==  weapon) skin(skinID, skinSeed, statTrak, wear, quality)
}
