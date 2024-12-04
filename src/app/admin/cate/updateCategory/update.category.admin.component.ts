import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../../service/category.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-update-category-admin',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './update.category.admin.component.html',
  styleUrls: ['./update.category.admin.component.scss']
})
export class UpdateCategoryAdminComponent {
  @Input() category: any; // Nhận dữ liệu danh mục được chọn để cập nhật
  @Output() categoryUpdated = new EventEmitter<void>(); // Phát sự kiện khi cập nhật thành công

  updateCategoryForm: FormGroup;

  constructor(private fb: FormBuilder, private categoryService: CategoryService) {
    this.updateCategoryForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['']
    });
  }

  ngOnChanges(): void {
    if (this.category) {
      // Gán giá trị ban đầu cho form khi danh mục thay đổi
      this.updateCategoryForm.patchValue({
        name: this.category.name,
        description: this.category.description
      });
    }
  }

  onSubmit(): void {
    if (this.updateCategoryForm.valid) {
      const updatedData = this.updateCategoryForm.value;
      this.categoryService.updateCategory(this.category.id, updatedData).subscribe({
        next: () => {
          alert('Category updated successfully!');
          this.categoryUpdated.emit(); // Phát sự kiện cập nhật thành công
        },
        error: (err: { error: { message: any; }; }) => {
          console.error('Error updating category:', err);
          alert(err?.error?.message || 'Failed to update category.');
        }
      });
    } else {
      alert('Please fill in the form correctly.');
    }
  }
}
