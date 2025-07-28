import {Component, EventEmitter, Input, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-error-panel',
  standalone: true,
  templateUrl: './error-panel.component.html',
  styleUrls: ['./error-panel.component.css'],
  imports: [],
  animations: [
    trigger('expandCollapse', [
      state('visible', style({
        height: '*',
        opacity: 1,
        padding: '*',
        margin: '*',
        overflow: 'hidden'
      })),
      state('hidden', style({
        height: '0px',
        opacity: 0,
        padding: '0px',
        margin: '0px',
        overflow: 'hidden'
      })),
      transition('hidden => visible', [
        animate('300ms ease-out')
      ]),
      transition('visible => hidden', [
        animate('300ms ease-in')
      ]),
    ])
  ]
})
export class ErrorPanelComponent {
  @Input() error?: string;
  @Output() close = new EventEmitter<void>();

  get isVisible() {
    return !!this.error && this.error.trim().length > 0;
  }

  onClose() {
    this.close.emit();
  }
}
