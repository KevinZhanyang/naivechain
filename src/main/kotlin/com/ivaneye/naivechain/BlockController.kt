package com.ivaneye.naivechain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockController {

    @Autowired
    private lateinit var blockService: BlockService

    @Autowired
    private lateinit var p2pService: P2PService

    @RequestMapping(value = "/block", method = [(RequestMethod.GET)])
    fun blocks(): ResponseEntity<List<Block>> {
        return ResponseEntity.ok(blockService.getBlockChain())
    }

    @RequestMapping(value = "/block", method = [(RequestMethod.POST)])
    fun mineBlock(data: String): ResponseEntity<Block> {
        val newBlock = blockService.generateNextBlock(data)
        blockService.addBlock(newBlock)
        p2pService.broatcast(p2pService.responseLatestMsg())
        return ResponseEntity.ok(newBlock)
    }

    @RequestMapping(value = "/peer", method = [(RequestMethod.GET)])
    fun peers(): ResponseEntity<String> {
        return ResponseEntity.ok(p2pService.sessions.toString())
    }

    @RequestMapping(value = "/peer", method = [(RequestMethod.POST)])
    fun addPeer(peer: String): ResponseEntity<String> {
        p2pService.conn(peer)
        return ResponseEntity.ok("")
    }
}