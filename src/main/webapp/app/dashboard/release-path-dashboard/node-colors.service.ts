import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class NodeColorsService {
  private defaultColor = '#97C2FC';
  private activeColor = '#61dd45';
  private colors: string[] = ['#d33682', '#cb4b16', '#268bd2', '#2aa198', '#839496', '#b58900', '#002b36', '#9b479f'];

  constructor() {}

  getActiveColor(): string {
    return this.activeColor;
  }

  getColor(index: number): string {
    if (index < 0 || index > this.colors.length) {
      return this.defaultColor;
    } else {
      return this.colors[index];
    }
  }
}
