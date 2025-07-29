import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgClass, NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-date-picker',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    NgClass
  ],
  templateUrl: './date-picker.component.html',
  styleUrl: './date-picker.component.css'
})
export class DatePickerComponent implements OnInit {
  @Output() pickedDate = new EventEmitter<string>();

  selectedDate: string = '';
  currentYear!: number;
  currentMonth!: number;
  daysInMonth: { day: number | null }[] = [];
  monthNames = [
    'Styczeń', 'Luty', 'Marzec', 'Kwiecień',
    'Maj', 'Czerwiec', 'Lipiec', 'Sierpień',
    'Wrzesień', 'Październik', 'Listopad', 'Grudzień'
  ];
  dayNames = ['Pn', 'Wt', 'Śr', 'Cz', 'Pt', 'So', 'Nd'];

  ngOnInit() {
    const today = new Date();
    this.currentYear = today.getFullYear();
    this.currentMonth = today.getMonth();
    this.generateDays(this.currentYear, this.currentMonth);

    this.selectedDate = today.toISOString().substring(0, 10);
  }

  generateDays(year: number, month: number) {
    const days: { day: number | null }[] = [];

    const firstDayOfMonth = new Date(year, month, 1);
    let startDay = firstDayOfMonth.getDay();

    startDay = (startDay === 0) ? 6 : startDay - 1;

    for (let i = 0; i < startDay; i++) {
      days.push({ day: null });
    }

    const lastDay = new Date(year, month + 1, 0).getDate();
    for (let i = 1; i <= lastDay; i++) {
      days.push({ day: i });
    }

    this.daysInMonth = days;
  }


  prevMonth() {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.generateDays(this.currentYear, this.currentMonth);
  }

  nextMonth() {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.generateDays(this.currentYear, this.currentMonth);
  }

  getCurrentDate() {
    return this.monthNames.at(this.currentMonth) + ' ' + this.currentYear;
  }

  selectDate(day: number) {
    const month = (this.currentMonth + 1).toString().padStart(2, '0');
    const dayStr = day.toString().padStart(2, '0');
    this.selectedDate = `${this.currentYear}-${month}-${dayStr}`;
    this.pickedDate.emit(this.selectedDate);
  }

  isSelected(day: number): boolean {
    const month = (this.currentMonth + 1).toString().padStart(2, '0');
    const dayStr = day.toString().padStart(2, '0');
    const dateStr = `${this.currentYear}-${month}-${dayStr}`;
    return this.selectedDate === dateStr;
  }

}
