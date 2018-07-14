/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IpcTimeSheetAppTestModule } from '../../../test.module';
import { AgileTeamDeleteDialogComponent } from 'app/entities/agile-team/agile-team-delete-dialog.component';
import { AgileTeamService } from 'app/entities/agile-team/agile-team.service';

describe('Component Tests', () => {
    describe('AgileTeam Management Delete Component', () => {
        let comp: AgileTeamDeleteDialogComponent;
        let fixture: ComponentFixture<AgileTeamDeleteDialogComponent>;
        let service: AgileTeamService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [IpcTimeSheetAppTestModule],
                declarations: [AgileTeamDeleteDialogComponent]
            })
                .overrideTemplate(AgileTeamDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AgileTeamDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AgileTeamService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
