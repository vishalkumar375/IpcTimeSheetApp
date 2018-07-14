/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IpcTimeSheetAppTestModule } from '../../../test.module';
import { ProjectCodeDetailComponent } from 'app/entities/project-code/project-code-detail.component';
import { ProjectCode } from 'app/shared/model/project-code.model';

describe('Component Tests', () => {
    describe('ProjectCode Management Detail Component', () => {
        let comp: ProjectCodeDetailComponent;
        let fixture: ComponentFixture<ProjectCodeDetailComponent>;
        const route = ({ data: of({ projectCode: new ProjectCode(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [IpcTimeSheetAppTestModule],
                declarations: [ProjectCodeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProjectCodeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProjectCodeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.projectCode).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
