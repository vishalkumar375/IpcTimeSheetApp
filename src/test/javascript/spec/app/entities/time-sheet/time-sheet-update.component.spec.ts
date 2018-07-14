/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { IpcTimeSheetAppTestModule } from '../../../test.module';
import { TimeSheetUpdateComponent } from 'app/entities/time-sheet/time-sheet-update.component';
import { TimeSheetService } from 'app/entities/time-sheet/time-sheet.service';
import { TimeSheet } from 'app/shared/model/time-sheet.model';

describe('Component Tests', () => {
    describe('TimeSheet Management Update Component', () => {
        let comp: TimeSheetUpdateComponent;
        let fixture: ComponentFixture<TimeSheetUpdateComponent>;
        let service: TimeSheetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [IpcTimeSheetAppTestModule],
                declarations: [TimeSheetUpdateComponent]
            })
                .overrideTemplate(TimeSheetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TimeSheetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimeSheetService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TimeSheet(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.timeSheet = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TimeSheet();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.timeSheet = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
