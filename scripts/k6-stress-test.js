import { describe } from 'https://jslib.k6.io/functional/0.0.3/index.js';
import { Httpx, Request, Get, Post } from 'https://jslib.k6.io/httpx/0.0.2/index.js';
import { randomIntBetween, randomItem } from "https://jslib.k6.io/k6-utils/1.1.0/index.js";
import http from 'k6/http';

export let options = {
  vus: 20,
  duration: '1m'
};

const pdfFile = open('./bestand.pdf', 'b');

let session = new Httpx({baseURL: 'http://localhost:8080'});

export default function testSuite() {

  describe('01. Fetching books', (t) => {
    let responses = session.batch([
      new Get('/books/1'),
      new Get('/books/2'),
      new Get('/books/3')

    ], {
      tags: {name: 'Books'},
    });

    responses.forEach(response => {
      t.expect(response.status).as("response status").toEqual(200)
        .and(response).toHaveValidJson();
    });
  })

  describe(`02. Create a test author`, (t) => {
    const payload = JSON.stringify({
        firstName: "Jules",
        lastName: "Vernes"
    });
    const params = {
	    headers: {
	      'Content-Type': 'application/json',
	    },
	  };
    
    let resp = session.post(`/authors`, payload, params);

    t.expect(resp.status).as("Author creation status").toEqual(200)
      .and(resp).toHaveValidJson();

    session.newAuthorId=resp.json('id');
  });

  describe(`03. Create a test book`, (t) => {
    const payload = JSON.stringify({
        title: "Voyage au Centre de la Terre",
        pages: 311,
        isbn: "978-2-253-01254-2",
        year: "2001",
        language: "Francais",
        authors_id: [session.newAuthorId]
    });

    const params = {
	    headers: {
	      'Content-Type': 'application/json',
	    },
	  };
    
    let resp = session.post(`/books`, payload, params);

    t.expect(resp.status).as("content creation status").toEqual(200)
      .and(resp).toHaveValidJson();

    session.newBookId=resp.json('id');
  })


  describe('04. Upload book content', (t) => {
    const data = {
        file: http.file(pdfFile, "bestand.pdf",'application/pdf'),
    };

    let resp = session.post(`/books/${session.newBookId}/content`, data);

    t.expect(resp.status).as("Content creation status").toEqual(200)
      .and(resp).toHaveValidJson();
    
  })


  describe('05. List all authors', (t) => {

    let resp = session.get(`/authors`);

    t.expect(resp.status).as("Authors fetch status").toEqual(200)
      .and(resp).toHaveValidJson();
  })

  describe('06. List all book contents', (t) => {

    let resp = session.get(`/books/content`);

    t.expect(resp.status).as("Books content fetch status").toEqual(200)
      .and(resp).toHaveValidJson();
  })

  describe('07. List all books', (t) => {

    let resp = session.get(`/authors`);

    t.expect(resp.status).as("Books fetch status").toEqual(200)
      .and(resp).toHaveValidJson();
  })

  describe('08. List all authors', (t) => {

    let resp = session.get(`/authors`);

    t.expect(resp.status).as("Books fetch status").toEqual(200)
      .and(resp).toHaveValidJson();
  })

  describe('09. Delete book content', (t) => {

    let response = session.delete(`/books/${session.newBookId}/content`);

    t.expect(response.status).as("Book delete response status").toEqual(200);
  })


}
