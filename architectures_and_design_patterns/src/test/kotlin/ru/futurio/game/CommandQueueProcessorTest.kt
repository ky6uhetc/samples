package ru.futurio.game

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.*
import ru.futurio.game.command.Command
import ru.futurio.game.command.CommandContext
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class CommandQueueProcessorTest {

    private val commandContext = mock<CommandContext> { }
    private val commandErrorProcessor = mock<CommandErrorProcessor> { }
    private val commandQueueProcessor = CommandQueueProcessor(commandErrorProcessor)

    @Test
    fun `happy path`() {
        val commandsList = List<Command<*>>(3) { mock { } }
        val commandQueue = LinkedBlockingQueue<Command<*>?>().also { q -> q.addAll(commandsList) }

        commandQueueProcessor.process(commandContext, commandQueue)

        assertEquals(0, commandQueue.size)
        commandsList.forEach { command -> verify(command, times(1)).execute(commandContext) }
        verifyNoInteractions(commandErrorProcessor)
    }

    @Test
    fun `processing with some erroneous commands`() {
        val commandsList = listOf<Command<*>>(
            mock { whenever(it.execute(any())).thenThrow(RuntimeException("something went wrong")) },
            mock { }
        )
        val commandQueue = LinkedBlockingQueue<Command<*>?>().also { q -> q.addAll(commandsList) }

        assertDoesNotThrow {
            commandQueueProcessor.process(commandContext, commandQueue)
        }

        assertEquals(0, commandQueue.size)
        commandsList.forEach { command -> verify(command, times(1)).execute(commandContext) }
        val exceptionCaptor = argumentCaptor<Exception>()
        val commandCaptor = argumentCaptor<Command<*>>()
        verify(commandErrorProcessor, times(1)).handle(exceptionCaptor.capture(), commandCaptor.capture())
        assertInstanceOf(RuntimeException::class.java, exceptionCaptor.firstValue)
        verifyNoMoreInteractions(commandErrorProcessor)
    }

    @Test
    fun `processing is interrupted`() {
        val countDownLatch1 = CountDownLatch(1)
        val countDownLatch2 = CountDownLatch(1)
        val countDownLatch3 = CountDownLatch(1)
        val commandsList = listOf<Command<*>>(
            mock { whenever(it.execute(any())).then { countDownLatch1.countDown(); countDownLatch2.await() } },
            mock { }
        )
        val commandQueue = LinkedBlockingQueue<Command<*>?>().also { q -> q.addAll(commandsList) }

        val commandProcessorThread = thread {
            commandQueueProcessor.process(commandContext, commandQueue)
            countDownLatch3.countDown()
        }
        countDownLatch1.await()
        commandProcessorThread.interrupt()
        countDownLatch3.await()

        assertEquals(1, commandQueue.size)
        verify(commandsList[0], times(1)).execute(commandContext)
        verifyNoInteractions(commandsList[1])

        val exceptionCaptor = argumentCaptor<Exception>()
        verify(commandErrorProcessor, times(1)).handle(exceptionCaptor.capture(), any())
        assertInstanceOf(InterruptedException::class.java, exceptionCaptor.firstValue)
    }
}
