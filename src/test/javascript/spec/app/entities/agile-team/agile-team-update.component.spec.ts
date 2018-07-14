/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { IpcTimeSheetAppTestModule } from '../../../test.module';
import { AgileTeamUpdateComponent } from 'app/entities/agile-team/agile-team-update.component';
import { AgileTeamService } from 'app/entities/agile-team/agile-team.service';
import { AgileTeam } from 'app/shared/model/agile-team.model';

describe('Component Tests', () => {
    describe('AgileTeam Management Update Component', () => {
        let comp: AgileTeamUpdateComponent;
        let fixture: ComponentFixture<AgileTeamUpdateComponent>;
        let service: AgileTeamService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [IpcTimeSheetAppTestModule],
                declarations: [AgileTeamUpdateComponent]
            })
                .overrideTemplate(AgileTeamUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AgileTeamUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AgileTeamService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AgileTeam(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.agileTeam = entity;
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
                    const entity = new AgileTeam();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.agileTeam = entity;
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
