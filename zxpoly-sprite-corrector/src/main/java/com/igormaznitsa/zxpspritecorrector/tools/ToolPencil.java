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

package com.igormaznitsa.zxpspritecorrector.tools;

import com.igormaznitsa.zxpspritecorrector.components.EditorComponent;
import java.awt.Rectangle;

public class ToolPencil extends AbstractTool {
  private static final long serialVersionUID = 1486692252806983383L;

  public ToolPencil(){
    super("Pencil.GIF", "Pencil allows to set pixels of defined color");
  }

  @Override
  public void process(EditorComponent editComponent, Rectangle area, int mouseKeys, int controlKeys) {
  }
}
