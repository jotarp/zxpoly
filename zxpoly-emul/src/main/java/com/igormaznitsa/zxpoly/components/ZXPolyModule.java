/*
 * Copyright (C) 2014 Raydac Research Group Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.igormaznitsa.zxpoly.components;

import com.igormaznitsa.z80.Z80;
import com.igormaznitsa.z80.Z80CPUBus;
import java.util.Arrays;
import java.util.logging.Logger;

public final class ZXPolyModule implements IODevice, Z80CPUBus {
  private final Logger LOGGER;
  
  private final Motherboard board;
  private final int moduleIndex;

  private final Z80 cpu;

  private final int PORT_REG0;
  private final int PORT_REG1;
  private final int PORT_REG2;
  private final int PORT_REG3;

  private final int[] zxPolyRegsWritten = new int[4];

  private int port7FFD;
  private int lastM1Address;

  private int registerReadingCounter = 3;
  private static final int Z80_PASSIVESIGNALS = Z80.SIGNAL_IN_nINT | Z80.SIGNAL_IN_nNMI | Z80.SIGNAL_IN_nRESET | Z80.SIGNAL_IN_nWAIT;

  private boolean localReset;
  private boolean localInt;
  private boolean localNmi;
  private boolean waitSignal;

  private boolean stopAddressWait;
  
  private static int calcRegAddress(final int reg) {
    return (reg << 12) | (reg << 8) | 0xFF;
  }

  public ZXPolyModule(final Motherboard board, final int index) {
    this.board = board;
    this.moduleIndex = index;

    this.PORT_REG0 = calcRegAddress(0);
    this.PORT_REG1 = calcRegAddress(1);
    this.PORT_REG2 = calcRegAddress(2);
    this.PORT_REG3 = calcRegAddress(3);

    this.cpu = new Z80(this);

    this.LOGGER = Logger.getLogger("ZX#" + index);

    LOGGER.info("Inited");
  }

  private int getRAMOffsetInHeap() {
    return (this.zxPolyRegsWritten[0] & 7) * 0x10000;
  }

  @Override
  public int readIO(final ZXPolyModule module, final int port) {
    final int result;
    final int mappedModule = this.board.getMappedCPUIndex();

    if (module != this && this.moduleIndex > 0 && this.moduleIndex == mappedModule) {
      // reading memory for IO offset and make notification through INT
      result = this.board.readRAM(module, getRAMOffsetInHeap() + port);
      prepareLocalInt();
    }
    else {
      if (port == PORT_REG0) {
        final int cpuState = this.cpu.getState();
        final int addr = ((this.lastM1Address >> 1) & 0x1)
                | ((this.lastM1Address >> 2) & 0x2)
                | ((this.lastM1Address >> 8) & 0x4)
                | ((this.lastM1Address >> 12) & 0x8)
                | ((this.lastM1Address >> 14) & 0x10)
                | ((this.lastM1Address >> 15) & 0x20);

        result = ((cpuState & Z80.SIGNAL_OUT_nHALT) == 0 ? 0 : ZXPOLY_rREG0_HALTMODE)
                | (this.waitSignal ? ZXPOLY_rREG0_WAITMODE : 0) | (addr << 2);
      }
      else {
        result = 0;
      }
    }
    return result;
  }

  @Override
  public void writeIO(final ZXPolyModule module, final int port, final int value) {
    final int mappedModule = this.board.getMappedCPUIndex();
    if (module != this && this.moduleIndex > 0 && this.moduleIndex == mappedModule) {
      // writing IO data in memory of mapped module with notification
      this.board.writeRAM(module, getRAMOffsetInHeap() + port, value);
      prepareLocalNMI();
    }
    else if ((this.board.get3D00() & PORTw_ZXPOLY_BLOCK) == 0 && module.moduleIndex <= this.moduleIndex) {
      if (port == PORT_REG0) {
        this.zxPolyRegsWritten[0] = value;
        if ((value & ZXPOLY_wREG0_RESET) != 0) {
          prepareLocalReset();
        }
        if ((value & ZXPOLY_wREG0_NMI) != 0) {
          prepareLocalNMI();
        }
        if ((value & ZXPOLY_wREG0_INT) != 0) {
          prepareLocalInt();
        }
      }
      else if (port == PORT_REG1) {
        this.zxPolyRegsWritten[1] = value;
      }
      else if (port == PORT_REG2) {
        this.zxPolyRegsWritten[2] = value;
      }
      else if (port == PORT_REG3) {
        this.zxPolyRegsWritten[3] = value;
      }
    }
  }

  @Override
  public Motherboard getMotherboard() {
    return this.board;
  }

  public void prepareLocalReset() {
    this.localReset = true;
    this.registerReadingCounter = 3;
  }

  public void prepareLocalNMI() {
    this.localNmi = true;
  }

  public void prepareLocalInt() {
    this.localInt = true;
  }

  public boolean step(final boolean signalReset, final boolean commonInt) {
    final int s_reset = signalReset || this.localReset ? Z80.SIGNAL_IN_nRESET : 0;
    this.localReset = false;

    final int s_int;
    if (this.moduleIndex == 0) {
      s_int = commonInt || this.localInt ? Z80.SIGNAL_IN_nINT : 0;
    }
    else {
      s_int = ((this.board.get3D00() & PORTw_ZXPOLY_BLOCK) == 0 ? false : commonInt) || this.localInt ? Z80.SIGNAL_IN_nINT : 0;
    }
    this.localInt = false;

    final int s_nmi = (this.zxPolyRegsWritten[1] & ZXPOLY_wREG1_DISABLE_NMI) == 0 ? (this.localNmi ? Z80.SIGNAL_IN_nNMI : 0) : 0;
    this.localNmi = false;

    final int s_wait = this.waitSignal ? Z80.SIGNAL_IN_nWAIT : 0;

    final int oldCpuState = this.cpu.getState();
    this.cpu.step(Z80_PASSIVESIGNALS ^ s_reset ^ s_int ^ s_wait ^ s_nmi);
    final int newCpuState = this.cpu.getState();
    
    final boolean metHalt = (newCpuState & Z80.SIGNAL_OUT_nHALT) == 0 && (oldCpuState & Z80.SIGNAL_OUT_nHALT)!=0;
    return metHalt;
  }

  public int readVideoMemory(final int videoOffset) {
    final int moduleRamOffsetInHeap = (this.zxPolyRegsWritten[0] & 7) * 0x10000;

    final int result;
    if ((this.port7FFD & PORTw_ZX128_SCREEN) == 0) {
      // RAM 3
      result = this.board.readRAM(this, 0xC000 + moduleRamOffsetInHeap + videoOffset);
    }
    else {
      // RAM 7
      result = this.board.readRAM(this, 0x1C000 + moduleRamOffsetInHeap + videoOffset);
    }
    return result;
  }

  @Override
  public byte readMemory(final Z80 cpu, final int address, final boolean m1) {
    final int moduleRamOffsetInHeap = (this.zxPolyRegsWritten[0] & 7) * 0x10000;

    if (m1) {
      this.lastM1Address = address;
    }

    if (m1 && this.board.is3D00NotLocked() && this.registerReadingCounter==0){
      this.stopAddressWait = address != 0 && address == (this.zxPolyRegsWritten[2] | (this.zxPolyRegsWritten[3]<<8));
    }
    
    final byte result;
    if (this.registerReadingCounter > 0) {
      // read local registers R1-R3 instead of memory
      this.registerReadingCounter--;
      result = (byte) this.zxPolyRegsWritten[3 - this.registerReadingCounter];
    }
    else {
      switch (address >>> 14) {
        case 0: {
          if ((this.port7FFD & PORTw_ZX128_ROMRAM) == 0) {
            // ROM section
            result = (byte) this.board.readROM(((this.port7FFD & PORTw_ZX128_ROM) == 0 ? 0 : 0x4000) | address);
          }
          else {
            //RAM0
            result = (byte) this.board.readRAM(this, moduleRamOffsetInHeap + address);
          }
        }
        break;
        case 1: {
          // CPU1, RAM5
          result = (byte) this.board.readRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | 0x14000));
        }
        break;
        case 2: {
          // CPU2, RAM2
          result = (byte) this.board.readRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | 0x8000));
        }
        break;
        case 3: {
          //CPU3, top page
          result = (byte) this.board.readRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | (0x4000 * (this.port7FFD & 0x7))));
        }
        break;
        default:
          throw new Error("Unexpected memory address [" + address + ']');
      }
    }
    return result;
  }

  @Override
  public void writeMemory(final Z80 cpu, final int address, final byte data) {
    final int reg0 = this.zxPolyRegsWritten[0];

    if ((reg0 & REG0w_MEMORY_WRITING_DISABLED) == 0) {
      final int moduleRamOffsetInHeap = (reg0 & 7) * 0x10000;

      switch (address >>> 14) {
        case 0: {
          if ((this.port7FFD & PORTw_ZX128_ROMRAM) != 0) {
            //RAM0
            this.board.writeRAM(this, moduleRamOffsetInHeap + address, data);
          }
        }
        break;
        case 1: {
          // CPU1, RAM5
          this.board.writeRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | 0x14000), data);
        }
        break;
        case 2: {
          // CPU2, RAM2
          this.board.writeRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | 0x8000), data);
        }
        break;
        case 3: {
          //CPU3, top page
          this.board.writeRAM(this, moduleRamOffsetInHeap + ((address & 0x3FFF) | (0x4000 * (this.port7FFD & 0x7))), data);
        }
        break;
        default:
          throw new Error("Unexpected memory address [" + address + ']');
      }
    }
  }

  @Override
  public byte readPort(final Z80 cpu, final int port) {
    final byte result;
    if (port == PORTrw_ZXPOLY) {
      result = 0;
    }
    else {
      result = (byte) this.board.readBusIO(this, port);
    }
    return result;
  }

  private void set7FFD(final int value) {
    if ((this.port7FFD & PORTw_ZX128_LOCK) == 0) {
      this.port7FFD = value;
    }
  }

  @Override
  public void writePort(final Z80 cpu, final int port, final byte data) {
    final int reg0 = this.zxPolyRegsWritten[0];
    if ((reg0 & ZXPOLY_wREG0_OUT_DISABLED) == 0) {
      final int value = data & 0xFF;
      if (port == PORTw_ZX128) {
        if (this.moduleIndex == 0) {
          if (this.board.getMappedCPUIndex() > 0) {
            this.board.writeBusIO(this, port, value);
          }
          else {
            set7FFD(data);
          }
        }
        else {
          set7FFD(data);
        }
      }
      else {
        this.board.writeBusIO(this, port, value);
      }
    }
  }

  @Override
  public byte onCPURequestDataLines(final Z80 cpu) {
    return (byte) 0xFF;
  }

  @Override
  public void onRETI(final Z80 cpu) {
  }

  @Override
  public void preStep(final boolean signalReset, final boolean signalInt) {
    if (signalReset) {
      setStateAfterReset();
    }
    prepareWaitSignal();
  }

  private void prepareWaitSignal(){
    this.waitSignal = this.stopAddressWait || (this.moduleIndex>0 && this.board.isCPUModules123InWaitMode());
  }
  
  @Override
  public String getName() {
    return "ZXM#" + moduleIndex;
  }

  public int getReg1WrittenData(){
    return this.zxPolyRegsWritten[1];
  }
  
  private void setStateAfterReset() {
    LOGGER.info("Reset");
    this.port7FFD = 0;
    this.registerReadingCounter = 0;
    this.localReset = false;
    this.lastM1Address = 0;
    this.localInt = false;
    this.localNmi = false;
    Arrays.fill(this.zxPolyRegsWritten, 0);
    this.zxPolyRegsWritten[0] = this.moduleIndex << 1; // set the memory offset in the heap
    prepareWaitSignal();
  }

}
