package com.tkblackbelt.game.server.npc.scripts

import akka.actor.ActorRef
import com.tkblackbelt.game.constants.MapIDs
import com.tkblackbelt.game.models.database.{GamePortals, RespawnPoints}
import com.tkblackbelt.game.server.client.ClientHandler.TeleportTo
import com.tkblackbelt.game.server.npc.NPCActor.NpcInteraction
import com.tkblackbelt.java.npc.{ResponseInit, Response}


/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 charlesbenger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Desert City conductress will teleport clients to tc, mc, and market
 */
class DCConductress_10051 extends NpcScript {

  lazy val market = RespawnPoints.respawnPoints.get(MapIDs.Market).get
  lazy val tc     = GamePortals.findPortal(MapIDs.DesertCity, MapIDs.TwinCity)
  lazy val mc     = TeleportTo(MapIDs.DesertCity, 85, 321)
  val id = List(100051)


  override def handle(implicit event: NpcInteraction, client: ActorRef){
    method(event.linkID).invoke(this, event, client)

    @ResponseInit
    def init() {
      Options(
        "Welcome to the Desert City Conductress. Where would you like to go?",
        "No Where",
        "Twin City" -> method("toTC"),
        "Market" -> method("toMarket"),
        "Mystic Castle" -> method("toMC")
      )
    }

    @Response
    def toTC() =
      sendToClient(TeleportTo(tc.fromMap, tc.fromX - 10, tc.fromY - 10))

    @Response
    def toMarket() =
      sendToClient(TeleportTo(market.revivemapid, market.revivex, market.revivey))

    @Response
    def toMC() =
      sendToClient(mc)
  }
}
