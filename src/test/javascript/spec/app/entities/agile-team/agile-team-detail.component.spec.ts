/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IpcTimeSheetAppTestModule } from '../../../test.module';
import { AgileTeamDetailComponent } from 'app/entities/agile-team/agile-team-detail.component';
import { AgileTeam } from 'app/shared/model/agile-team.model';

describe('Component Tests', () => {
    describe('AgileTeam Management Detail Component', () => {
        let comp: AgileTeamDetailComponent;
        let fixture: ComponentFixture<AgileTeamDetailComponent>;
        const route = ({ data: of({ agileTeam: new AgileTeam(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [IpcTimeSheetAppTestModule],
                declarations: [AgileTeamDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AgileTeamDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AgileTeamDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.agileTeam).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
