import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {faComments} from '@fortawesome/free-solid-svg-icons';
import {PostModel} from "../post-model";

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css']
})
export class PostTileComponent implements OnInit {

  faComments = faComments;
  //Декоратор, который помечает поле класса как входное свойство и предоставляет метаданные конфигурации.
  //Свойство input привязано к свойству DOM в шаблоне.
  //Во время обнаружения изменений Angular автоматически обновляет свойство data значением свойства DOM.
  @Input() posts: PostModel[];

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
