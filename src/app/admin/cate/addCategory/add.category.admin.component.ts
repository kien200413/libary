import { Component,EventEmitter,Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Thêm Validators
import { CategoryService } from '../../../service/category.service';
import { ReactiveFormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-category-admin',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add.category.admin.component.html',
  styleUrls: ['./add.category.admin.component.scss'],
})
export class AddCategoryAdminComponent {
  categoryForm: FormGroup;

  // Phát sự kiện khi thêm danh mục thành công
  @Output() productAdded = new EventEmitter<void>();

  constructor(private fb: FormBuilder, private categoryService: CategoryService) {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
    });
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.categoryService.addCategory(this.categoryForm.value).subscribe({
        next: () => {
          // Hiển thị thông báo
          alert('Thêm thành công');
          // Phát sự kiện để component cha cập nhật
          this.productAdded.emit();
          // Reset form sau khi thêm
          this.categoryForm.reset();
        },
        error: (err: { error: { message: string; }; }) => {
          // Xử lý lỗi
          const errorMessage = err?.error?.message || 'Thêm thất bại.';
          alert(errorMessage);
          console.error('Error adding category:', err);
        },
      });
    } else {
      alert('Please fill in the form correctly.');
    }
  }
  
  }
  
  

