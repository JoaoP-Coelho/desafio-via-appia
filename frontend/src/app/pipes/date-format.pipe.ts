import { formatDate } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appDate',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {

  transform(value: string | Date | null | undefined): string {
    if (!value) {
      return '-';
    }

    return formatDate(value, 'dd/MM/yyyy HH:mm', 'en-US');
  }
}
